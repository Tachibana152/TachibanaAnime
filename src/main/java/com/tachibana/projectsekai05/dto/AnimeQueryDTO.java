package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动漫分页查询入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "动漫分页查询")
public class AnimeQueryDTO extends PageQuery {

    @Schema(description = "分类: NEW(一月新番) / CLASSIC(经典动画)", example = "NEW")
    private String category;

    @Schema(description = "关键词：匹配标题/日文名/原作/导演/制作/简介/内容", example = "芙莉莲")
    private String keyword;
}