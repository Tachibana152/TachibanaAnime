package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserRoleDTO;
import com.tachibana.projectsekai05.dto.UserStatusDTO;

/**
 * 用户管理服务（超级管理员）
 */
public interface UserAdminService {

    /**
     * 分页查询用户（含角色/状态）
     */
    PageResult<UserInfoVO> pageUsers(UserQueryDTO query);

    /**
     * 启用/禁用用户
     */
    void updateStatus(Long id, UserStatusDTO dto);

    /**
     * 修改用户角色
     */
    void updateRole(Long id, UserRoleDTO dto);

    /**
     * 删除用户
     */
    void delete(Long id);
}