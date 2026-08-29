package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动漫评论
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime_comment")
public class AnimeComment extends BaseEntity {

    /** 动漫ID */
    private Long animeId;

    /** 评论人ID */
    private Long userId;

    /** 评论内容 */
    private String content;

    /** 点赞数 */
    private Integer likeCount;
}