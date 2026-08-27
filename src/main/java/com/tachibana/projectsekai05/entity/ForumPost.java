package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 论坛帖子
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_post")
public class ForumPost extends BaseEntity {

    /** 作者ID */
    private Long userId;

    /** 标题 */
    private String title;

    /** 正文（多段，空行分段） */
    private String content;

    /** 来源链接 */
    private String sourceUrl;

    /** 状态：0 待审核 / 1 已发布 / 2 已驳回 */
    private Integer status;

    /** 驳回原因 */
    private String rejectReason;

    /** 是否置顶：0 否 / 1 是 */
    private Integer top;

    /** 浏览量 */
    private Long viewCount;

    /** 回复数（冗余计数） */
    private Integer replyCount;
}
