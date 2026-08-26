package com.tachibana.projectsekai05.common.utils;

import java.util.regex.Pattern;

/**
 * 密码强度校验工具
 */
public final class PasswordValidator {

    private PasswordValidator() {
    }

    /**
     * 校验密码必须同时包含字母和数字，且至少 8 位
     */
    public static boolean isCharacterAndNumber(String password) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$";
        return Pattern.matches(pattern, password);
    }
}