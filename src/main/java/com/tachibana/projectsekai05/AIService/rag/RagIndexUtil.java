package com.tachibana.projectsekai05.AIService.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 向量入库工具：将文档切分 → 嵌入 → 以确定性 id 覆盖写入 Redis 向量库。
 * <p>
 * 向量 id 形如 {@code {idPrefix}:{序号}}，写入走 {@code JSON.SET} 覆盖语义，
 * 同一前缀重复写入不会产生重复向量；也可按前缀清理旧向量（删除/重灌）。
 */
@Component
@Slf4j
public class RagIndexUtil {

    /** 与 RedisEmbeddingStore 保持一致的 key 前缀 */
    private static final String STORE_PREFIX = "embedding:";

    private static final int MAX_SEGMENT_SIZE = 500;
    private static final int MAX_OVERLAP_SIZE = 100;
    private static final int SCAN_COUNT = 100;

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final UnifiedJedis jedis;

    public RagIndexUtil(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel, UnifiedJedis jedis) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
        this.jedis = jedis;
    }

    /**
     * 将单个文档切分、嵌入并写入向量库。
     *
     * @param document   文档
     * @param idPrefix   向量 id 前缀（不含序号），如 {@code anime:1} / {@code doc:CLANNAD}
     * @param clearFirst 是否先清除该前缀下的旧向量
     */
    public void indexDocument(Document document, String idPrefix, boolean clearFirst) {
        if (document == null || document.text() == null || document.text().isBlank()) {
            return;
        }
        if (clearFirst) {
            clearPrefix(idPrefix);
        }
        DocumentSplitter splitter = DocumentSplitters.recursive(MAX_SEGMENT_SIZE, MAX_OVERLAP_SIZE);
        List<TextSegment> segments = splitter.split(document);
        if (segments.isEmpty()) {
            return;
        }
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        List<String> ids = new ArrayList<>(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            ids.add(idPrefix + ":" + i);
        }
        embeddingStore.addAll(ids, embeddings, segments);
        log.debug("RAG 入库 {} 个片段，idPrefix={}", segments.size(), idPrefix);
    }

    /**
     * 扫描并删除某个 id 前缀下的所有向量（如删除动画时清理 {@code anime:1:*}）。
     */
    public void clearPrefix(String idPrefix) {
        String match = STORE_PREFIX + idPrefix + ":*";
        Set<String> keys = new HashSet<>();
        String cursor = "0";
        do {
            ScanResult<String> result = jedis.scan(cursor, new ScanParams().match(match).count(SCAN_COUNT));
            keys.addAll(result.getResult());
            cursor = result.getCursor();
        } while (!"0".equals(cursor));
        if (!keys.isEmpty()) {
            jedis.del(keys.toArray(new String[0]));
            log.debug("RAG 清理 {} 个旧向量，idPrefix={}", keys.size(), idPrefix);
        }
    }
}