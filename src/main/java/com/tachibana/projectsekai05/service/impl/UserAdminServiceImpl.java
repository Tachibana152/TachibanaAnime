package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务实现（占位，待实现）
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {
@Resource
private SysUserMapper sysUserMapper;
@Resource
private RedisTemplate redisTemplate;
    @Override
    public PageResult<UserInfoVO> pageUsers(UserQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void updateStatus(Long id, UserStatusDTO dto) {
        if (id.equals(UserContext.getUserId())) {
            throw new BusinessException(400, "不能操作自己的账号");
        }
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        sysUser.setStatus(dto.getStatus());
        sysUserMapper.updateById(sysUser);
        redisTemplate.delete(RedisConstants.TOKEN_PREFIX + id);
        redisTemplate.delete(RedisConstants.USER_LOGIN_PREFIX + id);
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