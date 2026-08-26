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

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}