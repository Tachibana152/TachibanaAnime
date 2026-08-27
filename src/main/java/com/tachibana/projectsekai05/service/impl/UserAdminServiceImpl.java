package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserRoleDTO;
import com.tachibana.projectsekai05.dto.UserStatusDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.UserAdminService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理服务实现（超级管理员）
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserAdminServiceImpl(SysUserMapper sysUserMapper, RedisTemplate<String, Object> redisTemplate) {
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageResult<UserInfoVO> pageUsers(UserQueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StringUtils.hasText(query.getNickname()), SysUser::getNickname, query.getNickname())
                .orderByDesc(SysUser::getId);
        IPage<SysUser> page = sysUserMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<UserInfoVO> records = page.getRecords().stream().map(UserInfoVO::from).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
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
    }

    @Override
    public void updateRole(Long id, UserRoleDTO dto) {
        if (id.equals(UserContext.getUserId())) {
            throw new BusinessException(400, "不能降低自己的角色");
        }
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        sysUser.setRole(dto.getRole());
        sysUserMapper.updateById(sysUser);
        redisTemplate.opsForValue().set(RedisConstants.USER_LOGIN_PREFIX + id, UserVO.from(sysUser),
                RedisConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    @Override
    public void delete(Long id) {
        if (id.equals(UserContext.getUserId())) {
            throw new BusinessException(400, "不能删除自己的账号");
        }
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        sysUserMapper.deleteById(id);
        redisTemplate.delete(RedisConstants.TOKEN_PREFIX + id);
        redisTemplate.delete(RedisConstants.USER_LOGIN_PREFIX + id);
    }
}