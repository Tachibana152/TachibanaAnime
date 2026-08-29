package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    /** 个人简介 */
    private String bio;

    /** 当前头像URL */
    private String avatar;

    /** 待审核头像URL（超管审核通过后转正） */
    private String avatarPending;

    /** 角色：USER / ADMIN / SUPER_ADMIN */
    private String role;

    /** 状态：1 正常，0 禁用 */
    private Integer status;
}