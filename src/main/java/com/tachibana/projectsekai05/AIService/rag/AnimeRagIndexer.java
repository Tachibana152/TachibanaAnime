package com.tachibana.projectsekai05.AIService.rag;

import com.tachibana.projectsekai05.entity.Anime;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动漫 → RAG 向量库索引器。
 * <p>
 * 在动漫新增/编辑成功后调用 {@link #indexAnime(Anime)}，删除时调用 {@link #removeAnime(Long)}。
 * 向量 id 使用确定性前缀 {@code anime:{id}}，重复提交覆盖不重复。
 * 向量库异常不影响动漫主流程（内部捕获并记录 WARN）。
 */
@Component
@Slf4j
public class AnimeRagIndexer {

    private final RagIndexUtil ragIndexUtil;

    public AnimeRagIndexer(RagIndexUtil ragIndexUtil) {
        this.ragIndexUtil = ragIndexUtil;
    }

    /**
     * 将一部动漫的全部文本信息写入向量库（编辑时覆盖旧向量）。
     */
    public void indexAnime(Anime anime) {
        try {
            if (anime == null || anime.getId() == null) {
                return;
            }
            Document document = buildDocument(anime);
            ragIndexUtil.indexDocument(document, "anime:" + anime.getId(), true);
            log.info("动漫已加入 RAG 向量库: id={}, title={}", anime.getId(), anime.getTitle());
        } catch (Exception e) {
            log.warn("动漫加入 RAG 向量库失败 animeId={}: {}",
                    anime == null ? null : anime.getId(), e.getMessage());
        }
    }

    /**
     * 从向量库移除一部动漫的全部向量。
     */
    public void removeAnime(Long animeId) {
        try {
            ragIndexUtil.clearPrefix("anime:" + animeId);
            log.info("动漫已从 RAG 向量库移除: id={}", animeId);
        } catch (Exception e) {
            log.warn("动漫从 RAG 向量库移除失败 animeId={}: {}", animeId, e.getMessage());
        }
    }

    /**
     * 将动漫各文本字段拼成一个知识文档（跳过空值），并附带 animeId/title/category 元数据。
     */
    private Document buildDocument(Anime anime) {
        Map<String, Object> parts = new LinkedHashMap<>();
        parts.put("标题", anime.getTitle());
        parts.put("日文名", anime.getTitleJp());
        parts.put("分类", anime.getCategory());
        parts.put("原作", anime.getOriginal());
        parts.put("导演", anime.getDirector());
        parts.put("脚本", anime.getWriter());
        parts.put("制作", anime.getProduction());
        parts.put("话数", anime.getEpisodes() == null ? null : anime.getEpisodes() + " 话");
        parts.put("首播", anime.getAirDate());
        parts.put("播出星期", anime.getAirWeekday());
        parts.put("别名", anime.getAlias());
        parts.put("分镜", anime.getStoryboard());
        parts.put("演出", anime.getPerformance());
        parts.put("音乐", anime.getMusic());
        parts.put("人物原案", anime.getCharaOriginal());
        parts.put("人物设定", anime.getCharaDesign());
        parts.put("系列构成", anime.getSeriesComposition());
        parts.put("美术监督", anime.getArtDirector());
        parts.put("色彩设计", anime.getColorDesign());
        parts.put("总作画监督", anime.getChiefAnimationDirector());
        parts.put("作画监督", anime.getAnimationDirector());
        parts.put("摄影监督", anime.getPhotographyDirector());
        parts.put("企画", anime.getPlanning());
        parts.put("简介", anime.getSynopsis());
        parts.put("内容", anime.getContent());
        parts.put("语录", anime.getQuote());

        StringBuilder sb = new StringBuilder();
        parts.forEach((label, value) -> {
            if (value != null && !value.toString().isBlank()) {
                sb.append(label).append(": ").append(value).append('\n');
            }
        });

        Metadata metadata = Metadata.from("animeId", String.valueOf(anime.getId()))
                .put("title", anime.getTitle() == null ? "" : anime.getTitle())
                .put("category", anime.getCategory() == null ? "" : anime.getCategory());
        return Document.from(sb.toString(), metadata);
    }
}