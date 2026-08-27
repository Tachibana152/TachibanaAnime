package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 论坛回复
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_reply")
public class ForumReply extends BaseEntity {

    /** 帖子ID */
    private Long postId;

    /** 回复人ID */
    private Long userId;

    /** 回复内容 */
    private String content;
}
