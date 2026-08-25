package com.tachibana.projectsekai05.common.utils;

import com.tachibana.projectsekai05.common.constant.CommonConstants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具（SHA-256 + 盐）
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * 生成密码哈希
     */
    public static String encode(String rawPassword) {
        return encode(rawPassword, CommonConstants.PASSWORD_SALT);
    }

    /**
     * 生成密码哈希（自定义盐）
     */
    public static String encode(String rawPassword, String salt) {
        return sha256(salt + rawPassword);
    }

    /**
     * 校验密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encodedPassword.equals(encode(rawPassword));
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) {
                    hex.append('0');
                }
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 算法不可用", e);
        }
    }
}
