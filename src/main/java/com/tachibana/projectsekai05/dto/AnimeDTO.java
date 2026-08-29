package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 动漫新增/编辑入参
 */
@Data
@Schema(description = "动漫新增/编辑入参")
public class AnimeDTO implements Serializable {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "葬送的芙莉莲Ⅱ")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "日文名", example = "葬送のフリーレン 第2期")
    private String titleJp;

    @Schema(description = "分类: NEW / CLASSIC", example = "NEW")
    private String category;

    @Schema(description = "封面图片URL", example = "/uploads/anime/Frieren2nd.jpg")
    private String cover;

    @Schema(description = "网页背图URL（详情页大图背景）", example = "/uploads/anime/ef_background.jpg")
    private String background;

    @Schema(description = "原作")
    private String original;

    @Schema(description = "导演")
    private String director;

    @Schema(description = "脚本")
    private String writer;

    @Schema(description = "话数")
    private Integer episodes;

    @Schema(description = "放送开始时间", example = "2026年1月16日")
    private String airDate;

    @Schema(description = "放送星期", example = "星期五")
    private String airWeekday;

    @Schema(description = "制作公司")
    private String production;

    @Schema(description = "简介")
    private String synopsis;

    @Schema(description = "内容/分集介绍（支持换行）")
    private String content;

    @Schema(description = "分镜")
    private String storyboard;

    @Schema(description = "演出")
    private String performance;

    @Schema(description = "音乐")
    private String music;

    @Schema(description = "人物原案")
    private String charaOriginal;

    @Schema(description = "人物设定")
    private String charaDesign;

    @Schema(description = "系列构成")
    private String seriesComposition;

    @Schema(description = "美术监督")
    private String artDirector;

    @Schema(description = "色彩设计")
    private String colorDesign;

    @Schema(description = "总作画监督")
    private String chiefAnimationDirector;

    @Schema(description = "作画监督")
    private String animationDirector;

    @Schema(description = "摄影监督")
    private String photographyDirector;

    @Schema(description = "企画")
    private String planning;

    @Schema(description = "别名")
    private String alias;

    @Schema(description = "语录")
    private String quote;

    @Schema(description = "内容贡献者ID列表（选填；保存时会合并当前操作人）")
    private java.util.List<Long> contributorIds;
}