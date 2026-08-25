package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;

/**
 * 认证服务
 */
public interface AuthService {

    /**
     * 登录，成功后返回 JWT
     */
    LoginVO login(LoginDTO loginDTO);
}