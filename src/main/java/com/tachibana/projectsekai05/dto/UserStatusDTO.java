package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户状态修改入参
 */
@Data
@Schema(description = "用户状态修改入参")
public class UserStatusDTO implements Serializable {

    @Schema(description = "状态: 1正常 0禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态取值错误")
    @Max(value = 1, message = "状态取值错误")
    private Integer status;
}