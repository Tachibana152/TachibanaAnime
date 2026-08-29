package com.tachibana.projectsekai05.dto;

import com.tachibana.projectsekai05.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "待审核头像URL")
    private String avatarPending;

    @Schema(description = "角色: USER / ADMIN / SUPER_ADMIN", example = "USER")
    private String role;

    @Schema(description = "状态: 1正常 0禁用")
    private Integer status;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    /**
     * 用户实体 -> 用户信息 VO 转换（脱敏，不含密码）。
     * 约定：同名字段由 BeanUtils 自动拷贝；新增字段请保持实体与 VO 同名，否则需在此手动 set。
     */
    public static UserInfoVO from(SysUser user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}