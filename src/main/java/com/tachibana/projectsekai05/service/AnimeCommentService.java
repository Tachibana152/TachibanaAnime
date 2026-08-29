package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeCommentDTO;
import com.tachibana.projectsekai05.dto.AnimeCommentVO;
import com.tachibana.projectsekai05.dto.PageQuery;

/**
 * 动漫评论服务
 */
public interface AnimeCommentService {

    /**
     * 分页查询动漫评论（时间倒序，最新在前）
     */
    PageResult<AnimeCommentVO> pageComments(Long animeId, PageQuery query);

    /**
     * 发表评论（需登录）
     */
    AnimeCommentVO create(Long animeId, AnimeCommentDTO dto);

    /**
     * 删除评论（作者本人或管理员）
     */
    void delete(Long id);

    /**
     * 点赞/取消点赞（toggle），返回最新评论信息（含 likeCount/liked）
     */
    AnimeCommentVO toggleLike(Long id);
}