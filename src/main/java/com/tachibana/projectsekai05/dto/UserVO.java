package com.tachibana.projectsekai05.dto;

import com.tachibana.projectsekai05.entity.SysUser;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户出参（脱敏，不含密码）
 */
@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private Integer status;

    private LocalDateTime createTime;

    public static UserVO from(SysUser user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}