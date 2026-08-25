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
}