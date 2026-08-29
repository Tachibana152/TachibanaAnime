package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.PostConstants;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AdminPostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.ReviewDTO;
import com.tachibana.projectsekai05.entity.ForumPost;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.ForumPostMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.service.AdminPostService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子审核服务实现
 */
@Service
public class AdminPostServiceImpl implements AdminPostService {

    private final ForumPostMapper forumPostMapper;
    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public AdminPostServiceImpl(ForumPostMapper forumPostMapper, SysUserMapper sysUserMapper,
                                RedisTemplate<String, Object> redisTemplate) {
        this.forumPostMapper = forumPostMapper;
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageResult<PostVO> pagePosts(AdminPostQueryDTO query) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getStatus() != null, ForumPost::getStatus, query.getStatus());
        wrapper.orderByDesc(ForumPost::getCreateTime);
        IPage<ForumPost> page = forumPostMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<PostVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PostVO detail(Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return toVO(post);
    }

    @Override
    public PostVO review(Long id, ReviewDTO dto) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        int status = dto.getStatus();
        if (status == PostConstants.STATUS_REJECTED) {
            if (!StringUtils.hasText(dto.getRejectReason())) {
                throw new BusinessException(400, "驳回时必须填写驳回原因");
            }
            post.setRejectReason(dto.getRejectReason());
        } else {
            post.setRejectReason("");
        }
        post.setStatus(status);
        post.setUpdateTime(LocalDateTime.now());
        forumPostMapper.updateById(post);
        redisTemplate.delete(RedisConstants.CACHE_POST_PREFIX + id);
        return toVO(post);
    }

    /**
     * 帖子实体 -> VO 转换。
     * 约定：同名字段由 BeanUtils 自动拷贝；username 为派生字段（关联用户表查询昵称），需手动填充。
     * 新增字段请保持实体与 VO 同名，否则需在此手动 set。
     */
    private PostVO toVO(ForumPost post) {
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);
        vo.setUsername(displayName(post.getUserId()));
        return vo;
    }

    private String displayName(Long userId) {
        if (userId == null) {
            return "";
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            return "";
        }
        return StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getUsername();
    }
}
