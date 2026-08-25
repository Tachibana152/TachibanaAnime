package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AdminPostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.ReviewDTO;

/**
 * 帖子审核服务（管理员）
 */
public interface AdminPostService {

    /**
     * 按状态分页查询帖子（审核队列）
     */
    PageResult<PostVO> pagePosts(AdminPostQueryDTO query);

    /**
     * 帖子详情（含未发布帖，供管理员预览）
     */
    PostVO detail(Long id);

    /**
     * 审核帖子：通过(1) / 驳回(2)，驳回需填原因
     */
    PostVO review(Long id, ReviewDTO dto);
}