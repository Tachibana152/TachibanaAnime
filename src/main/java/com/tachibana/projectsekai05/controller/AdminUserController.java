package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.AvatarReviewDTO;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserRoleDTO;
import com.tachibana.projectsekai05.dto.UserStatusDTO;
import com.tachibana.projectsekai05.security.RequireRole;
import com.tachibana.projectsekai05.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理接口（超级管理员）
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/admin/users")
@RequireRole(SecurityConstants.ROLE_SUPER_ADMIN)
public class AdminUserController {

    private final UserAdminService userAdminService;

    public AdminUserController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @Operation(summary = "用户分页列表", description = "需要 SUPER_ADMIN，支持用户名/昵称搜索")
    @GetMapping
    public R<PageResult<UserInfoVO>> page(UserQueryDTO query) {
        return R.success(userAdminService.pageUsers(query));
    }

    @Operation(summary = "启用/禁用用户", description = "需要 SUPER_ADMIN，不能操作自己")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UserStatusDTO dto) {
        userAdminService.updateStatus(id, dto);
        return R.success();
    }

    @Operation(summary = "修改用户角色", description = "需要 SUPER_ADMIN，不能降低自己的角色")
    @PutMapping("/{id}/role")
    public R<Void> updateRole(@PathVariable Long id, @Valid @RequestBody UserRoleDTO dto) {
        userAdminService.updateRole(id, dto);
        return R.success();
    }

    @Operation(summary = "删除用户", description = "需要 SUPER_ADMIN，不能删除自己")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userAdminService.delete(id);
        return R.success();
    }

    @Operation(summary = "待审核头像列表", description = "需要 SUPER_ADMIN")
    @GetMapping("/avatar-audits")
    public R<List<UserInfoVO>> avatarAudits() {
        return R.success(userAdminService.listAvatarAudits());
    }

    @Operation(summary = "头像审核", description = "通过（转正）或驳回，需要 SUPER_ADMIN")
    @PutMapping("/avatar-audits/{id}")
    public R<Void> reviewAvatar(@PathVariable Long id, @Valid @RequestBody AvatarReviewDTO dto) {
        userAdminService.reviewAvatar(id, dto.isApprove());
        return R.success();
    }
}