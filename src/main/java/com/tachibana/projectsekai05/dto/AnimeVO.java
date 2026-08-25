package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动漫出参
 */
@Data
@Schema(description = "动漫信息")
public class AnimeVO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "日文名")
    private String titleJp;

    @Schema(description = "分类: NEW / CLASSIC")
    private String category;

    @Schema(description = "封面URL")
    private String cover;

    @Schema(description = "原作")
    private String original;

    @Schema(description = "导演")
    private String director;

    @Schema(description = "脚本")
    private String writer;

    @Schema(description = "话数")
    private Integer episodes;

    @Schema(description = "放送开始")
    private String airDate;

    @Schema(description = "放送星期")
    private String airWeekday;

    @Schema(description = "制作公司")
    private String production;

    @Schema(description = "简介")
    private String synopsis;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}