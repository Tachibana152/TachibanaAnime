package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserRoleDTO;
import com.tachibana.projectsekai05.dto.UserStatusDTO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.UserAdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务实现（占位，待实现）
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {
@Resource
private SysUserMapper sysUserMapper;

    @Override
    public PageResult<UserInfoVO> pageUsers(UserQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void updateStatus(Long id, UserStatusDTO dto) {
        SysUser sysUser = sysUserMapper.selectById(id);

        return;
    }

    @Override
    public void updateRole(Long id, UserRoleDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }
}