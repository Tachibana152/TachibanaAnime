package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.PostConstants;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PostDTO;
import com.tachibana.projectsekai05.dto.PostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.entity.ForumPost;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.ForumPostMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.ForumPostService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 论坛帖子服务实现
 */
@Service
public class ForumPostServiceImpl implements ForumPostService {

    private final ForumPostMapper forumPostMapper;
    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public ForumPostServiceImpl(ForumPostMapper forumPostMapper, SysUserMapper sysUserMapper,
                                RedisTemplate<String, Object> redisTemplate) {
        this.forumPostMapper = forumPostMapper;
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageResult<PostVO> pagePosts(PostQueryDTO query) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getStatus, PostConstants.STATUS_PUBLISHED);
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(ForumPost::getTitle, query.getKeyword())
                    .or().like(ForumPost::getContent, query.getKeyword()));
        }
        wrapper.orderByDesc(ForumPost::getTop).orderByDesc(ForumPost::getCreateTime);
        IPage<ForumPost> page = forumPostMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<PostVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PostVO detail(Long id) {
        Object cached = redisTemplate.opsForValue().get(RedisConstants.CACHE_POST_PREFIX + id);
        PostVO vo;
        if (cached instanceof PostVO postVO) {
            vo = postVO;
        } else {
            ForumPost post = forumPostMapper.selectById(id);
            if (post == null || post.getStatus() != PostConstants.STATUS_PUBLISHED) {
                throw new BusinessException(ResultCode.NOT_FOUND);
            }
            vo = toVO(post);
            redisTemplate.opsForValue().set(RedisConstants.CACHE_POST_PREFIX + id, vo,
                    RedisConstants.CACHE_POST_EXPIRE, TimeUnit.SECONDS);
        }
        redisTemplate.opsForValue().increment(RedisConstants.COUNT_POST_PREFIX + id);
        forumPostMapper.update(null, new LambdaUpdateWrapper<ForumPost>()
                .eq(ForumPost::getId, id)
                .setSql("view_count = view_count + 1"));
        vo.setViewCount(vo.getViewCount() == null ? 1L : vo.getViewCount() + 1L);
        return vo;
    }

    @Override
    public PageResult<PostVO> myPosts(PostQueryDTO query) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getUserId, userId);
        wrapper.orderByDesc(ForumPost::getCreateTime);
        IPage<ForumPost> page = forumPostMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<PostVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PostVO create(PostDTO dto) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        boolean autoApproved = isAdminRole(role);

        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setSourceUrl(dto.getSourceUrl() == null ? "" : dto.getSourceUrl());
        post.setStatus(autoApproved ? PostConstants.STATUS_PUBLISHED : PostConstants.STATUS_PENDING);
        post.setRejectReason("");
        post.setTop(PostConstants.NOT_TOP);
        post.setViewCount(0L);
        post.setReplyCount(0);
        post.setDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        post.setCreateTime(now);
        post.setUpdateTime(now);
        forumPostMapper.insert(post);

        PostVO vo = toVO(post);
        vo.setAutoApproved(autoApproved);
        return vo;
    }

    @Override
    public PostVO update(Long id, PostDTO dto) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!post.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setSourceUrl(dto.getSourceUrl() == null ? "" : dto.getSourceUrl());
        if (post.getStatus() == PostConstants.STATUS_PENDING || post.getStatus() == PostConstants.STATUS_REJECTED) {
            post.setStatus(PostConstants.STATUS_PENDING);
            post.setRejectReason("");
        }
        post.setUpdateTime(LocalDateTime.now());
        forumPostMapper.updateById(post);
        redisTemplate.delete(RedisConstants.CACHE_POST_PREFIX + id);
        return toVO(post);
    }

    @Override
    public void delete(Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long currentUserId = UserContext.getUserId();
        String role = UserContext.getRole();
        if (!post.getUserId().equals(currentUserId) && !isAdminRole(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        forumPostMapper.deleteById(id);
        redisTemplate.delete(RedisConstants.CACHE_POST_PREFIX + id);
    }

    @Override
    public PostVO toggleTop(Long id, Integer top) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        post.setTop(top);
        post.setUpdateTime(LocalDateTime.now());
        forumPostMapper.updateById(post);
        redisTemplate.delete(RedisConstants.CACHE_POST_PREFIX + id);
        return toVO(post);
    }

    private boolean isAdminRole(String role) {
        return SecurityConstants.ROLE_ADMIN.equals(role) || SecurityConstants.ROLE_SUPER_ADMIN.equals(role);
    }

    private PostVO toVO(ForumPost post) {
        PostVO vo = new PostVO();
        vo.setId(post.getId());
        vo.setUserId(post.getUserId());
        vo.setUsername(displayName(post.getUserId()));
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setSourceUrl(post.getSourceUrl());
        vo.setStatus(post.getStatus());
        vo.setRejectReason(post.getRejectReason());
        vo.setTop(post.getTop());
        vo.setViewCount(post.getViewCount());
        vo.setReplyCount(post.getReplyCount());
        vo.setCreateTime(post.getCreateTime());
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
