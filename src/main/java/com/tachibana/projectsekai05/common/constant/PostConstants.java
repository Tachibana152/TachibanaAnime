package com.tachibana.projectsekai05.common.constant;

/**
 * 论坛帖子相关常量
 */
public final class PostConstants {

    private PostConstants() {
    }

    /** 状态：待审核 */
    public static final int STATUS_PENDING = 0;

    /** 状态：已发布 */
    public static final int STATUS_PUBLISHED = 1;

    /** 状态：已驳回 */
    public static final int STATUS_REJECTED = 2;

    /** 未置顶 */
    public static final int NOT_TOP = 0;

    /** 已置顶 */
    public static final int IS_TOP = 1;

    /** 标题最大长度 */
    public static final int TITLE_MAX_LENGTH = 60;

    /** 正文最大长度 */
    public static final int CONTENT_MAX_LENGTH = 5000;

    /** 驳回原因最大长度 */
    public static final int REJECT_REASON_MAX_LENGTH = 200;
}