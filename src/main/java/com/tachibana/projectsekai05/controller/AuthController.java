package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;
import com.tachibana.projectsekai05.security.NoAuth;
import com.tachibana.projectsekai05.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @NoAuth
    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return R.success(authService.login(loginDTO));
    }
}