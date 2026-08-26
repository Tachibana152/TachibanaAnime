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

    /** 动漫详情缓存 key 前缀 */
    public static final String CACHE_ANIME_PREFIX = "cache:anime:";

    /** 帖子详情缓存 key 前缀 */
    public static final String CACHE_POST_PREFIX = "cache:post:";

    /** 动漫浏览量计数 key 前缀 */
    public static final String COUNT_ANIME_PREFIX = "count:anime:";

    /** 帖子浏览量计数 key 前缀 */
    public static final String COUNT_POST_PREFIX = "count:post:";

    /** 默认缓存过期时间（秒） */
    public static final long DEFAULT_EXPIRE = 3600L;

    /** token 过期时间（秒），默认 2 小时 */
    public static final long TOKEN_EXPIRE = 7200L;

    /** 动漫详情缓存过期时间（秒） */
    public static final long CACHE_ANIME_EXPIRE = 600L;

    /** 帖子详情缓存过期时间（秒） */
    public static final long CACHE_POST_EXPIRE = 300L;
}
