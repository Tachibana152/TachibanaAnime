package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PostDTO;
import com.tachibana.projectsekai05.dto.PostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.service.ForumPostService;
import org.springframework.stereotype.Service;

/**
 * 论坛帖子服务实现（占位，待实现）
 */
@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Override
    public PageResult<PostVO> pagePosts(PostQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO detail(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PageResult<PostVO> myPosts(PostQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO create(PostDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO update(Long id, PostDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public PostVO toggleTop(Long id, Integer top) {
        throw new UnsupportedOperationException("接口待实现");
    }
}