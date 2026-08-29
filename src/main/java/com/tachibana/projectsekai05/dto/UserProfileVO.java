package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户主页出参（公开资料 + 统计）
 */
@Data
@Schema(description = "用户主页")
public class UserProfileVO implements Serializable {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "角色: USER / ADMIN / SUPER_ADMIN")
    private String role;

    @Schema(description = "状态: 1正常 0禁用")
    private Integer status;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "已发布帖子数")
    private Long postCount;

    @Schema(description = "贡献过的动漫数")
    private Long animeCount;
}