package com.tachibana.projectsekai05.security;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前登录用户上下文（ThreadLocal）
 */
public final class UserContext {

    private UserContext() {
    }

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = CONTEXT.get();
        if (map == null) {
            map = new HashMap<>();
            CONTEXT.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = CONTEXT.get();
        return map == null ? null : map.get(key);
    }

    public static Long getUserId() {
        Object value = get("userId");
        return value instanceof Number number ? number.longValue() : null;
    }

    public static String getUsername() {
        return (String) get("username");
    }

    public static String getRole() {
        return (String) get("role");
    }

    public static void clear() {
        CONTEXT.remove();
    }
}