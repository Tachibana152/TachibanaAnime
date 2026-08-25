package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 论坛帖子分页查询入参（公开列表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "论坛帖子分页查询")
public class PostQueryDTO extends PageQuery {

    @Schema(description = "关键词：匹配标题/正文", example = "冰菓")
    private String keyword;
}