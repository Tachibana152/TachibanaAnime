package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.utils.JwtUtil;
import com.tachibana.projectsekai05.common.utils.PasswordUtil;
import com.tachibana.projectsekai05.common.utils.PasswordValidator;
import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;
import com.tachibana.projectsekai05.dto.RegisterDTO;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserProfileDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;


    public AuthServiceImpl(SysUserMapper sysUserMapper, JwtUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.sysUserMapper = sysUserMapper;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginDTO.getUsername()));
        if (user == null || !PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(400, "账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        redisTemplate.opsForValue().set(RedisConstants.TOKEN_PREFIX + user.getId(), token,
                RedisConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(RedisConstants.USER_LOGIN_PREFIX + user.getId(), UserVO.from(user),
                RedisConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(UserVO.from(user));
        return vo;
    }

    // ===== 以下接口待实现（实现阶段补齐注册/当前用户逻辑）=====

    @Override
    public UserInfoVO register(RegisterDTO registerDTO) {
    UserInfoVO vo = new UserInfoVO();
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String nickname = registerDTO.getNickname();
        if(username==null||password==null||nickname==null){
            throw new BusinessException(400, "用户名、密码和昵称不能为空");
        }
        if (sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }
        if(username.length()>16){
            throw new BusinessException(400, "用户名长度不得超过15个字符");
        }
        if(nickname.length()>16){
            throw new BusinessException(400, "昵称长度不得超过15个字符");
        }
        boolean characterAndNumber = PasswordValidator.isCharacterAndNumber(password);
        if(!characterAndNumber){
            throw new BusinessException(400, "密码必须由字母和数字组成，并且长度大于等于8个字符");
        }

        LocalDateTime now = LocalDateTime.now();
        String role = "USER";
        Integer status = 1;
        vo.setUsername(username);
        vo.setCreateTime(now);
        vo.setNickname(nickname);
        vo.setStatus(status);
        vo.setRole(role);
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(password));
        user.setNickname(nickname);
        user.setRole(role);
        user.setStatus(status);
        user.setDeleted(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        try {
            sysUserMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(400, "用户名已存在");
        }
        return vo;
    }

    @Override
    public UserInfoVO me() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
    }
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
        throw new BusinessException(401, "未登录");
        }
        if (sysUser.getStatus() != null && sysUser.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(sysUser, vo);
        return vo;
    }

    @Override
    public UserInfoVO updateProfile(UserProfileDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "未登录");
        }
        if (dto.getNickname() != null) {
            String nickname = dto.getNickname().trim();
            if (nickname.isEmpty()) {
                throw new BusinessException(400, "昵称不能为空");
            }
            user.setNickname(nickname);
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio().trim());
        }
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        refreshLoginCache(user);

        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public UserInfoVO submitAvatar(String avatarUrl) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        if (!StringUtils.hasText(avatarUrl)) {
            throw new BusinessException(400, "头像不能为空");
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "未登录");
        }
        user.setAvatarPending(avatarUrl);
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        refreshLoginCache(user);

        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    /**
     * 刷新 Redis 登录缓存（login:user:{id}），使昵称/头像/角色即时生效
     */
    private void refreshLoginCache(SysUser user) {
        redisTemplate.opsForValue().set(RedisConstants.USER_LOGIN_PREFIX + user.getId(), UserVO.from(user),
                RedisConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }
    }