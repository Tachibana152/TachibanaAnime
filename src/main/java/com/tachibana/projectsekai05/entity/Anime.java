package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动漫
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime")
public class Anime extends BaseEntity {

    private String title;

    private String titleJp;

    /** 分类：NEW 一月新番 / CLASSIC 经典动画 */
    private String category;

    private String cover;

    private String original;

    private String director;

    private String writer;

    /** 话数 */
    private Integer episodes;

    private String airDate;

    private String airWeekday;

    private String production;

    private String synopsis;

    private String content;

    /** 分镜 */
    private String storyboard;

    /** 演出 */
    private String performance;

    /** 音乐 */
    private String music;

    /** 人物原案 */
    private String charaOriginal;

    /** 人物设定 */
    private String charaDesign;

    /** 系列构成 */
    private String seriesComposition;

    /** 美术监督 */
    private String artDirector;

    /** 色彩设计 */
    private String colorDesign;

    /** 总作画监督 */
    private String chiefAnimationDirector;

    /** 作画监督 */
    private String animationDirector;

    /** 摄影监督 */
    private String photographyDirector;

    /** 企画 */
    private String planning;

    /** 别名 */
    private String alias;

    /** 语录 */
    private String quote;

    /** 浏览量 */
    private Long viewCount;

    /** 列表排序（小在前） */
    private Integer sort;
}