package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeCommentDTO;
import com.tachibana.projectsekai05.dto.AnimeCommentVO;
import com.tachibana.projectsekai05.dto.PageQuery;
import com.tachibana.projectsekai05.entity.Anime;
import com.tachibana.projectsekai05.entity.AnimeComment;
import com.tachibana.projectsekai05.entity.CommentLike;
import com.tachibana.projectsekai05.entity.SysUser;
import com.tachibana.projectsekai05.mapper.AnimeCommentMapper;
import com.tachibana.projectsekai05.mapper.AnimeMapper;
import com.tachibana.projectsekai05.mapper.CommentLikeMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.AnimeCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动漫评论服务实现
 */
@Service
public class AnimeCommentServiceImpl implements AnimeCommentService {

    private final AnimeCommentMapper animeCommentMapper;
    private final AnimeMapper animeMapper;
    private final SysUserMapper sysUserMapper;
    private final CommentLikeMapper commentLikeMapper;

    public AnimeCommentServiceImpl(AnimeCommentMapper animeCommentMapper, AnimeMapper animeMapper,
                                   SysUserMapper sysUserMapper, CommentLikeMapper commentLikeMapper) {
        this.animeCommentMapper = animeCommentMapper;
        this.animeMapper = animeMapper;
        this.sysUserMapper = sysUserMapper;
        this.commentLikeMapper = commentLikeMapper;
    }

    @Override
    public PageResult<AnimeCommentVO> pageComments(Long animeId, PageQuery query) {
        if (animeMapper.selectById(animeId) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        LambdaQueryWrapper<AnimeComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnimeComment::getAnimeId, animeId)
                .orderByDesc(AnimeComment::getCreateTime);
        IPage<AnimeComment> page = animeCommentMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<Long> ids = page.getRecords().stream().map(AnimeComment::getId).toList();
        Set<Long> likedIds = likedTargetIds(ids);
        List<AnimeCommentVO> records = page.getRecords().stream()
                .map(c -> toVO(c, likedIds.contains(c.getId())))
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public AnimeCommentVO create(Long animeId, AnimeCommentDTO dto) {
        if (animeMapper.selectById(animeId) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long userId = UserContext.getUserId();
        AnimeComment comment = new AnimeComment();
        comment.setAnimeId(animeId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setLikeCount(0);
        comment.setDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        animeCommentMapper.insert(comment);
        return toVO(comment, false);
    }

    @Override
    public void delete(Long id) {
        AnimeComment comment = animeCommentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long currentUserId = UserContext.getUserId();
        String role = UserContext.getRole();
        if (!comment.getUserId().equals(currentUserId) && !isAdminRole(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        animeCommentMapper.deleteById(id);
    }

    @Override
    public AnimeCommentVO toggleLike(Long id) {
        AnimeComment comment = animeCommentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<CommentLike> wrapper = new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getTargetType, CommentLike.TYPE_ANIME_COMMENT)
                .eq(CommentLike::getTargetId, id)
                .eq(CommentLike::getUserId, userId);
        CommentLike exist = commentLikeMapper.selectOne(wrapper);
        if (exist != null) {
            commentLikeMapper.deleteById(exist.getId());
            animeCommentMapper.update(null, new LambdaUpdateWrapper<AnimeComment>()
                    .eq(AnimeComment::getId, id)
                    .gt(AnimeComment::getLikeCount, 0)
                    .setSql("like_count = like_count - 1"));
        } else {
            CommentLike like = new CommentLike();
            like.setTargetType(CommentLike.TYPE_ANIME_COMMENT);
            like.setTargetId(id);
            like.setUserId(userId);
            like.setCreateTime(LocalDateTime.now());
            commentLikeMapper.insert(like);
            animeCommentMapper.update(null, new LambdaUpdateWrapper<AnimeComment>()
                    .eq(AnimeComment::getId, id)
                    .setSql("like_count = like_count + 1"));
        }
        return toVO(animeCommentMapper.selectById(id), exist == null);
    }

    private boolean isAdminRole(String role) {
        return SecurityConstants.ROLE_ADMIN.equals(role) || SecurityConstants.ROLE_SUPER_ADMIN.equals(role);
    }

    /**
     * 查询当前用户已点赞的目标 ID 集合（类型=动漫评论）
     */
    private Set<Long> likedTargetIds(List<Long> targetIds) {
        Long userId = UserContext.getUserId();
        if (userId == null || targetIds.isEmpty()) {
            return Set.of();
        }
        return commentLikeMapper.selectList(new LambdaQueryWrapper<CommentLike>()
                        .eq(CommentLike::getTargetType, CommentLike.TYPE_ANIME_COMMENT)
                        .in(CommentLike::getTargetId, targetIds)
                        .eq(CommentLike::getUserId, userId))
                .stream().map(CommentLike::getTargetId).collect(Collectors.toSet());
    }

    /**
     * 评论实体 -> VO 转换。
     * 约定：同名字段由 BeanUtils 自动拷贝；username 为派生字段（关联用户表查询昵称），需手动填充。
     */
    private AnimeCommentVO toVO(AnimeComment comment, boolean liked) {
        AnimeCommentVO vo = new AnimeCommentVO();
        BeanUtils.copyProperties(comment, vo);
        vo.setUsername(displayName(comment.getUserId()));
        vo.setLiked(liked);
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