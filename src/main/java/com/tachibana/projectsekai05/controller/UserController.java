package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口（需登录，演示 JWT 认证 + 分页）
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "分页查询用户")
    @GetMapping
    public R<PageResult<UserVO>> page(@Valid UserQueryDTO query) {
        return R.success(sysUserService.pageUsers(query));
    }
}