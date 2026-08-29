package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.PostDTO;
import com.tachibana.projectsekai05.dto.PostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;

/**
 * 论坛帖子服务
 */
public interface ForumPostService {

    /**
     * 公开分页查询已发布帖子（置顶优先 + 时间倒序）
     */
    PageResult<PostVO> pagePosts(PostQueryDTO query);

    /**
     * 帖子详情（仅已发布，浏览量 +1）
     */
    PostVO detail(Long id);

    /**
     * 我的帖子（含待审核/驳回状态）
     */
    PageResult<PostVO> myPosts(PostQueryDTO query);

    /**
     * 某用户已发布的帖子分页（用户主页用）
     */
    PageResult<PostVO> pageByUser(Long userId, PostQueryDTO query);

    /**
     * 发帖（普通用户待审核；ADMIN/SUPER_ADMIN 直接发布）
     */
    PostVO create(PostDTO dto);

    /**
     * 编辑帖子（待审核/驳回的帖子编辑后重新进入待审核）
     */
    PostVO update(Long id, PostDTO dto);

    /**
     * 删除帖子（作者本人或管理员）
     */
    void delete(Long id);

    /**
     * 置顶/取消置顶（管理员）
     */
    PostVO toggleTop(Long id, Integer top);
}