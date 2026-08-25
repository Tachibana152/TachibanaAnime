package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PageQuery;
import com.tachibana.projectsekai05.dto.ReplyDTO;
import com.tachibana.projectsekai05.dto.ReplyVO;

/**
 * 回复服务
 */
public interface ReplyService {

    /**
     * 分页查询帖子回复（时间正序）
     */
    PageResult<ReplyVO> pageReplies(Long postId, PageQuery query);

    /**
     * 发表回复（需登录）
     */
    ReplyVO create(Long postId, ReplyDTO dto);

    /**
     * 删除回复（作者本人或管理员）
     */
    void delete(Long id);
}