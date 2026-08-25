package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息出参（含角色，用于注册/当前用户/用户管理）
 */
@Data
@Schema(description = "用户信息")
public class UserInfoVO implements Serializable {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "角色: USER / ADMIN / SUPER_ADMIN", example = "USER")
    private String role;

    @Schema(description = "状态: 1正常 0禁用")
    private Integer status;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;
}