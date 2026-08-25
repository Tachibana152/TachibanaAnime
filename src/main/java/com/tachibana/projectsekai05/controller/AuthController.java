package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;
import com.tachibana.projectsekai05.dto.RegisterDTO;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.security.NoAuth;
import com.tachibana.projectsekai05.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Operation(summary = "登录", description = "登录成功后返回 JWT token 与用户信息（含角色）")
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return R.success(authService.login(loginDTO));
    }

    @NoAuth
    @Operation(summary = "注册", description = "注册默认角色为 USER")
    @PostMapping("/register")
    public R<UserInfoVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return R.success(authService.register(registerDTO));
    }

    @Operation(summary = "当前登录用户", description = "返回当前登录用户信息，需携带 Bearer token")
    @GetMapping("/me")
    public R<UserInfoVO> me() {
        return R.success(authService.me());
    }
}