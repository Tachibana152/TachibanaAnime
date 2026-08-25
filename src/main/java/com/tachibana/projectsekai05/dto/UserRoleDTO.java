package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色修改入参
 */
@Data
@Schema(description = "用户角色修改入参")
public class UserRoleDTO implements Serializable {

    @Schema(description = "角色: USER / ADMIN / SUPER_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED, example = "ADMIN")
    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "USER|ADMIN|SUPER_ADMIN", message = "角色取值错误")
    private String role;
}