package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.tachibana.projectsekai05.common.constant.PostConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.UserBriefVO;
import com.tachibana.projectsekai05.dto.UserProfileVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.entity.AnimeContributor;
import com.tachibana.projectsekai05.entity.ForumPost;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.AnimeContributorMapper;
import com.tachibana.projectsekai05.mapper.ForumPostMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统用户服务实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final ForumPostMapper forumPostMapper;
    private final AnimeContributorMapper animeContributorMapper;

    public SysUserServiceImpl(ForumPostMapper forumPostMapper, AnimeContributorMapper animeContributorMapper) {
        this.forumPostMapper = forumPostMapper;
        this.animeContributorMapper = animeContributorMapper;
    }

    @Override
    public PageResult<UserVO> pageUsers(UserQueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StringUtils.hasText(query.getNickname()), SysUser::getNickname, query.getNickname())
                .orderByDesc(SysUser::getId);

        IPage<SysUser> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<UserVO> records = page.getRecords().stream().map(UserVO::from).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public UserProfileVO profile(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        UserProfileVO vo = new UserProfileVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setBio(user.getBio());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());

        Long postCount = forumPostMapper.selectCount(new LambdaQueryWrapper<ForumPost>()
                .eq(ForumPost::getUserId, id)
                .eq(ForumPost::getStatus, PostConstants.STATUS_PUBLISHED));
        Long animeCount = animeContributorMapper.selectCount(new LambdaQueryWrapper<AnimeContributor>()
                .eq(AnimeContributor::getUserId, id));
        vo.setPostCount(postCount);
        vo.setAnimeCount(animeCount);
        return vo;
    }

    @Override
    public List<UserBriefVO> listAdmins() {
        return this.list(new LambdaQueryWrapper<SysUser>()
                        .in(SysUser::getRole, "ADMIN", "SUPER_ADMIN")
                        .eq(SysUser::getStatus, 1))
                .stream().map(u -> {
                    UserBriefVO vo = new UserBriefVO();
                    vo.setId(u.getId());
                    vo.setUsername(u.getUsername());
                    vo.setNickname(u.getNickname());
                    vo.setAvatar(u.getAvatar());
                    return vo;
                }).toList();
    }
}