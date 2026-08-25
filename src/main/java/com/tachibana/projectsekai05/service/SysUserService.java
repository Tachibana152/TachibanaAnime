package com.tachibana.projectsekai05.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.common.result.PageResult;

/**
 * 系统用户服务
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     */
    PageResult<UserVO> pageUsers(UserQueryDTO query);
}