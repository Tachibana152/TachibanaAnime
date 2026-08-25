package com.tachibana.projectsekai05.common.constant;

/**
 * Redis 相关常量
 */
public final class RedisConstants {

    private RedisConstants() {
    }

    /** 登录用户信息缓存 key 前缀 */
    public static final String USER_LOGIN_PREFIX = "login:user:";

    /** 登录 token 缓存 key 前缀 */
    public static final String TOKEN_PREFIX = "login:token:";

    /** 默认缓存过期时间（秒） */
    public static final long DEFAULT_EXPIRE = 3600L;

    /** token 过期时间（秒），默认 2 小时 */
    public static final long TOKEN_EXPIRE = 7200L;
}
