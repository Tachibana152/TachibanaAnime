package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AdminPostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.ReviewDTO;
import com.tachibana.projectsekai05.service.AdminPostService;
import org.springframework.stereotype.Service;

/**
 * 帖子审核服务实现（占位，待实现）
 */
@Service
public class AdminPostServiceImpl implements AdminPostService {

    @Override
    public PageResult<PostVO> pagePosts(AdminPostQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO detail(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO review(Long id, ReviewDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }
}