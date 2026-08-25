package com.tachibana.projectsekai05.common.constant;

/**
 * 安全相关常量（JWT / 认证）
 */
public final class SecurityConstants {

    private SecurityConstants() {
    }

    /** Token 请求头 */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 管理员角色 */
    public static final String ROLE_ADMIN = "ADMIN";

    /** 普通用户角色 */
    public static final String ROLE_USER = "USER";

    /** 需要认证的路径前缀 */
    public static final String AUTH_PATH_PREFIX = "/api/**";

    /** 免认证路径（登录、文档等） */
    public static final String[] WHITE_LIST = {
            "/api/auth/login",
            "/doc.html",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico",
            "/error"
    };

    /** JWT 中用户名 claim 键 */
    public static final String CLAIM_USERNAME = "username";

    /** JWT 中用户ID claim 键 */
    public static final String CLAIM_USER_ID = "userId";

    /** JWT 中角色 claim 键 */
    public static final String CLAIM_ROLE = "role";
}
