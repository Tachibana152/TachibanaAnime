package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 提交头像入参
 */
@Data
@Schema(description = "提交头像")
public class AvatarDTO implements Serializable {

    @Schema(description = "头像图片URL", example = "/uploads/avatar/xxx.webp", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "头像不能为空")
    private String avatarUrl;
}