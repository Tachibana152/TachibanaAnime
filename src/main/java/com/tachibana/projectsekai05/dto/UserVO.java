package com.tachibana.projectsekai05.dto;

import com.tachibana.projectsekai05.entity.SysUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 用户出参（脱敏，不含密码）
 */
@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    /** 个人简介 */
    private String bio;

    /** 头像URL */
    private String avatar;

    /** 角色: USER / ADMIN / SUPER_ADMIN */
    private String role;

    private Integer status;

    private LocalDateTime createTime;

    /**
     * 用户实体 -> 登录出参 VO 转换（脱敏，不含密码）。
     * 约定：同名字段由 BeanUtils 自动拷贝；新增字段请保持实体与 VO 同名，否则需在此手动 set。
     */
    public static UserVO from(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}