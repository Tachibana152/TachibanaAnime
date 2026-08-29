package com.tachibana.projectsekai05.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.tachibana.projectsekai05.dto.UserBriefVO;
import com.tachibana.projectsekai05.dto.UserProfileVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.common.result.PageResult;

import java.util.List;

/**
 * 系统用户服务
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     */
    PageResult<UserVO> pageUsers(UserQueryDTO query);

    /**
     * 用户主页信息（含已发布帖子数 / 贡献动漫数统计）
     */
    UserProfileVO profile(Long id);

    /**
     * 管理员/超级管理员简要列表（动漫贡献者下拉用）
     */
    List<UserBriefVO> listAdmins();
}