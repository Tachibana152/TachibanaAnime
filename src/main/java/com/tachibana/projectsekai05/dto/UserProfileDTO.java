package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户资料修改入参（自己改昵称/简介）
 */
@Data
@Schema(description = "用户资料修改")
public class UserProfileDTO implements Serializable {

    @Schema(description = "昵称", example = "追番萌新")
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    @Schema(description = "个人简介", example = "热爱动画的追番人")
    @Size(max = 200, message = "简介长度不能超过200个字符")
    private String bio;
}