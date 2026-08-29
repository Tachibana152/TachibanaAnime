package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论点赞（全站评论去重）
 */
@Data
@TableName("comment_like")
public class CommentLike {

    public static final int TYPE_POST_REPLY = 1;
    public static final int TYPE_ANIME_COMMENT = 2;

    @TableId
    private Long id;

    /** 评论类型：1=论坛回复 2=动漫评论 */
    private Integer targetType;

    /** 评论ID */
    private Long targetId;

    /** 点赞用户ID */
    private Long userId;

    private LocalDateTime createTime;
}