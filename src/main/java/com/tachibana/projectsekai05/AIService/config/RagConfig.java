package com.tachibana.projectsekai05.AIService.config;

import com.tachibana.projectsekai05.AIService.rag.RagIndexUtil;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Easy RAG 配置
 * <p>
 * 知识库包含两类来源，共用同一个 Redis 向量库（索引 {@code embedding-index}）：
 * <ol>
 *   <li><b>文档库</b>：{@code easy-rag.document-path} 目录下的知识文档，启动时经 {@link RagIndexUtil}
 *       以确定性前缀 {@code doc:{文件名}} 覆盖写入；</li>
 *   <li><b>动漫库</b>：动漫新增/编辑时由 {@code AnimeRagIndexer} 以 {@code anime:{id}} 前缀写入，删除时清理。</li>
 * </ol>
 * <p>
 * <strong>注意：</strong>{@link RedisEmbeddingStore} 依赖 Redis 服务端的
 * <b>RediSearch</b> 与 <b>RedisJSON</b> 两个模块（可用 {@code redis/redis-stack-server} 镜像），
 * 否则启动时执行 {@code FT.LIST}/{@code FT.CREATE}/{@code JSON.SET} 会报错。
 * <p>
 * 随后注册 {@link ContentRetriever}，由 langchain4j-spring-boot4-starter 自动注入到
 * {@code @AiService} 的 Assistant 中——用户提问时会先检索相关文档/动漫内容再交给 LLM 回答。
 */
@Configuration
@Slf4j
public class RagConfig {

    /** 检索时最多返回的相关片段数 */
    private static final int MAX_RESULTS = 5;

    /** 检索相关度阈值（bge-small-en-v1.5 余弦相似度） */
    private static final double MIN_SCORE = 0.617;

    /** bge-small-en-v1.5-q 嵌入向量维度 */
    private static final int EMBEDDING_DIMENSION = 384;

    /** Redis 连接/读写超时（毫秒） */
    private static final int REDIS_TIMEOUT_MILLIS = 5000;

    /**
     * 嵌入模型：bge-small-en-v1.5 量化版，本地 ONNX 推理，无需外部服务。
     * 显式声明，避免依赖 SPI 自动发现（若 classpath 出现多个 EmbeddingModelFactory 会启动冲突）。
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return new BgeSmallEnV15QuantizedEmbeddingModel();
    }

    /**
     * 向量库专用 Jedis 客户端，向量库与索引清理共用。
     */
    @Bean
    public UnifiedJedis easyRagJedis(
            @Value("${easy-rag.redis.host:localhost}") String host,
            @Value("${easy-rag.redis.port:6379}") int port,
            @Value("${easy-rag.redis.password:}") String password) {
        DefaultJedisClientConfig.Builder jedisConfigBuilder = DefaultJedisClientConfig.builder()
                .connectionTimeoutMillis(REDIS_TIMEOUT_MILLIS)
                .socketTimeoutMillis(REDIS_TIMEOUT_MILLIS);
        if (password != null && !password.isBlank()) {
            jedisConfigBuilder.password(password);
        }
        return new UnifiedJedis(new HostAndPort(host, port), jedisConfigBuilder.build());
    }

    /**
     * Redis 向量库：构造时若索引不存在会立即执行 {@code FT.LIST}/{@code FT.CREATE}。
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(
            UnifiedJedis easyRagJedis,
            @Value("${easy-rag.redis.index-name:embedding-index}") String indexName) {
        return RedisEmbeddingStore.builder()
                .unifiedJedis(easyRagJedis)
                .indexName(indexName)
                .prefix("embedding:")
                .dimension(EMBEDDING_DIMENSION)
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(MAX_RESULTS)
                .minScore(MIN_SCORE)
                .build();
    }

    /**
     * 启动后加载并向量化知识文档。
     * <p>
     * 每个文档用确定性前缀 {@code doc:{文件名}} 覆盖写入（JSON.SET 覆盖，重启不重复），
     * 不全局清库——避免清掉运行时由 {@code AnimeRagIndexer} 写入的动漫向量。
     */
    @Bean
    public ApplicationRunner easyRagIngestor(
            RagIndexUtil ragIndexUtil,
            @Value("${easy-rag.document-path:./rag-docs}") String documentPath) {
        return args -> {
            Path path = Path.of(documentPath);
            if (!Files.exists(path)) {
                log.warn("Easy RAG 文档路径不存在: {}，跳过文档加载。请将文档放入该路径后重启。", path.toAbsolutePath());
                return;
            }

            List<Document> documents;
            if (Files.isDirectory(path)) {
                documents = FileSystemDocumentLoader.loadDocuments(path.toString());
            } else {
                documents = List.of(FileSystemDocumentLoader.loadDocument(path));
            }
            if (documents.isEmpty()) {
                log.warn("Easy RAG 文档目录为空: {}", path.toAbsolutePath());
                return;
            }

            long start = System.currentTimeMillis();
            for (Document document : documents) {
                ragIndexUtil.indexDocument(document, docIdPrefix(document), true);
            }
            log.info("Easy RAG 向量化完成 {} 个文档，耗时 {} ms", documents.size(), System.currentTimeMillis() - start);
        };
    }

    private String docIdPrefix(Document document) {
        String name = document.metadata().getString(Document.FILE_NAME);
        if (name == null || name.isBlank()) {
            return "doc:" + Integer.toHexString(document.text().hashCode());
        }
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }
        return "doc:" + name.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}