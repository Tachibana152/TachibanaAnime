package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.PostConstants;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PageQuery;
import com.tachibana.projectsekai05.dto.ReplyDTO;
import com.tachibana.projectsekai05.dto.ReplyVO;
import com.tachibana.projectsekai05.entity.ForumPost;
import com.tachibana.projectsekai05.entity.ForumReply;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.ForumPostMapper;
import com.tachibana.projectsekai05.mapper.ForumReplyMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.ReplyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 回复服务实现
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ForumReplyMapper forumReplyMapper;
    private final ForumPostMapper forumPostMapper;
    private final SysUserMapper sysUserMapper;

    public ReplyServiceImpl(ForumReplyMapper forumReplyMapper, ForumPostMapper forumPostMapper,
                            SysUserMapper sysUserMapper) {
        this.forumReplyMapper = forumReplyMapper;
        this.forumPostMapper = forumPostMapper;
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public PageResult<ReplyVO> pageReplies(Long postId, PageQuery query) {
        LambdaQueryWrapper<ForumReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumReply::getPostId, postId)
                .orderByAsc(ForumReply::getCreateTime);
        IPage<ForumReply> page = forumReplyMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<ReplyVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ReplyVO create(Long postId, ReplyDTO dto) {
        ForumPost post = forumPostMapper.selectById(postId);
        if (post == null || post.getStatus() != PostConstants.STATUS_PUBLISHED) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long userId = UserContext.getUserId();
        ForumReply reply = new ForumReply();
        reply.setPostId(postId);
        reply.setUserId(userId);
        reply.setContent(dto.getContent());
        reply.setDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        reply.setCreateTime(now);
        reply.setUpdateTime(now);
        forumReplyMapper.insert(reply);

        forumPostMapper.update(null, new LambdaUpdateWrapper<ForumPost>()
                .eq(ForumPost::getId, postId)
                .setSql("reply_count = reply_count + 1"));
        return toVO(reply);
    }

    @Override
    public void delete(Long id) {
        ForumReply reply = forumReplyMapper.selectById(id);
        if (reply == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long currentUserId = UserContext.getUserId();
        String role = UserContext.getRole();
        if (!reply.getUserId().equals(currentUserId) && !isAdminRole(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        forumReplyMapper.deleteById(id);
        forumPostMapper.update(null, new LambdaUpdateWrapper<ForumPost>()
                .eq(ForumPost::getId, reply.getPostId())
                .gt(ForumPost::getReplyCount, 0)
                .setSql("reply_count = reply_count - 1"));
    }

    private boolean isAdminRole(String role) {
        return SecurityConstants.ROLE_ADMIN.equals(role) || SecurityConstants.ROLE_SUPER_ADMIN.equals(role);
    }

    private ReplyVO toVO(ForumReply reply) {
        ReplyVO vo = new ReplyVO();
        vo.setId(reply.getId());
        vo.setPostId(reply.getPostId());
        vo.setUserId(reply.getUserId());
        vo.setUsername(displayName(reply.getUserId()));
        vo.setContent(reply.getContent());
        vo.setCreateTime(reply.getCreateTime());
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
