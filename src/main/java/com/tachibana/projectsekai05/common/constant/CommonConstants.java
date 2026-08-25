package com.tachibana.projectsekai05.common.constant;

/**
 * 通用常量
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    /** 是 */
    public static final int YES = 1;

    /** 否 */
    public static final int NO = 0;

    /** 默认分页大小 */
    public static final long DEFAULT_PAGE_SIZE = 10L;

    /** 默认页码 */
    public static final long DEFAULT_PAGE_NUM = 1L;

    /** 最大分页大小 */
    public static final long MAX_PAGE_SIZE = 100L;

    /** 升序 */
    public static final String SORT_ASC = "asc";

    /** 降序 */
    public static final String SORT_DESC = "desc";

    /** 字符集 UTF-8 */
    public static final String CHARSET_UTF8 = "UTF-8";

    /** 密码盐 */
    public static final String PASSWORD_SALT = "ProjectSekaiSalt#2026";
}
