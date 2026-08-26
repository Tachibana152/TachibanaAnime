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

    /** 角色：USER / ADMIN / SUPER_ADMIN */
    private String role;

    /** 状态：1 正常，0 禁用 */
    private Integer status;
}