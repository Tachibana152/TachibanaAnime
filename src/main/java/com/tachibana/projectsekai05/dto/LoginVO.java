package com.tachibana.projectsekai05.dto;

import lombok.Data;

/**
 * 登录出参
 */
@Data
public class LoginVO {

    private String token;

    private UserVO user;
}