package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;
import com.tachibana.projectsekai05.dto.RegisterDTO;
import com.tachibana.projectsekai05.dto.UserInfoVO;

/**
 * 认证服务
 */
public interface AuthService {

    /**
     * 登录，成功后返回 JWT 与用户信息
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 注册（默认 USER 角色）
     */
    UserInfoVO register(RegisterDTO registerDTO);

    /**
     * 获取当前登录用户信息
     */
    UserInfoVO me();
}