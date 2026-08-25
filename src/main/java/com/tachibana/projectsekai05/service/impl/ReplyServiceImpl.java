package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PageQuery;
import com.tachibana.projectsekai05.dto.ReplyDTO;
import com.tachibana.projectsekai05.dto.ReplyVO;
import com.tachibana.projectsekai05.service.ReplyService;
import org.springframework.stereotype.Service;

/**
 * 回复服务实现（占位，待实现）
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    @Override
    public PageResult<ReplyVO> pageReplies(Long postId, PageQuery query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public ReplyVO create(Long postId, ReplyDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }
}