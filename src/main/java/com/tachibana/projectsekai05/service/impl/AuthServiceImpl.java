package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.utils.JwtUtil;
import com.tachibana.projectsekai05.common.utils.PasswordUtil;
import com.tachibana.projectsekai05.common.utils.PasswordValidator;
import com.tachibana.projectsekai05.dto.LoginDTO;
import com.tachibana.projectsekai05.dto.LoginVO;
import com.tachibana.projectsekai05.dto.RegisterDTO;
import com.tachibana.projectsekai05.dto.UserInfoVO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;


    public AuthServiceImpl(SysUserMapper sysUserMapper, JwtUtil jwtUtil) {
        this.sysUserMapper = sysUserMapper;
        this.jwtUtil = jwtUtil;
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
        throw new UnsupportedOperationException("接口待实现");
    }
}