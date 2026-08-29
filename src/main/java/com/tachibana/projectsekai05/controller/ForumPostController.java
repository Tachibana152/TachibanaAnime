package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.PageQuery;
import com.tachibana.projectsekai05.dto.PostDTO;
import com.tachibana.projectsekai05.dto.PostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.ReplyDTO;
import com.tachibana.projectsekai05.dto.ReplyVO;
import com.tachibana.projectsekai05.dto.TopDTO;
import com.tachibana.projectsekai05.security.NoAuth;
import com.tachibana.projectsekai05.security.RequireRole;
import com.tachibana.projectsekai05.service.ForumPostService;
import com.tachibana.projectsekai05.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 论坛接口（帖子 + 回复）
 */
@Tag(name = "论坛")
@RestController
@RequestMapping("/api/forum/posts")
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final ReplyService replyService;

    public ForumPostController(ForumPostService forumPostService, ReplyService replyService) {
        this.forumPostService = forumPostService;
        this.replyService = replyService;
    }

    @NoAuth
    @Operation(summary = "帖子分页列表", description = "仅返回已发布帖子，置顶优先 + 时间倒序，支持关键词搜索")
    @GetMapping
    public R<PageResult<PostVO>> page(PostQueryDTO query) {
        return R.success(forumPostService.pagePosts(query));
    }

    @NoAuth
    @Operation(summary = "帖子详情", description = "仅已发布帖可访问，浏览量 +1")
    @GetMapping("/{id}")
    public R<PostVO> detail(@PathVariable Long id) {
        return R.success(forumPostService.detail(id));
    }

    @Operation(summary = "我的帖子", description = "需登录，包含待审核/已驳回状态")
    @GetMapping("/mine")
    public R<PageResult<PostVO>> mine(PostQueryDTO query) {
        return R.success(forumPostService.myPosts(query));
    }

    @Operation(summary = "发帖", description = "需登录。普通用户发帖进入待审核；ADMIN/SUPER_ADMIN 直接发布")
    @PostMapping
    public R<PostVO> create(@Valid @RequestBody PostDTO dto) {
        return R.success(forumPostService.create(dto));
    }

    @Operation(summary = "编辑帖子", description = "作者本人可编辑；待审核/驳回的帖子编辑后重新进入待审核")
    @PutMapping("/{id}")
    public R<PostVO> update(@PathVariable Long id, @Valid @RequestBody PostDTO dto) {
        return R.success(forumPostService.update(id, dto));
    }

    @Operation(summary = "删除帖子", description = "作者本人或 ADMIN/SUPER_ADMIN")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        forumPostService.delete(id);
        return R.success();
    }

    @RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
    @Operation(summary = "置顶/取消置顶", description = "需要 ADMIN 或 SUPER_ADMIN")
    @PutMapping("/{id}/top")
    public R<PostVO> toggleTop(@PathVariable Long id, @Valid @RequestBody TopDTO dto) {
        return R.success(forumPostService.toggleTop(id, dto.getTop()));
    }

    @NoAuth
    @Operation(summary = "帖子回复列表", description = "时间正序分页")
    @GetMapping("/{id}/replies")
    public R<PageResult<ReplyVO>> replies(@PathVariable Long id, PageQuery query) {
        return R.success(replyService.pageReplies(id, query));
    }

    @Operation(summary = "发表回复", description = "需登录")
    @PostMapping("/{id}/replies")
    public R<ReplyVO> createReply(@PathVariable Long id, @Valid @RequestBody ReplyDTO dto) {
        return R.success(replyService.create(id, dto));
    }

    @Operation(summary = "删除回复", description = "作者本人或 ADMIN/SUPER_ADMIN")
    @DeleteMapping("/replies/{id}")
    public R<Void> deleteReply(@PathVariable Long id) {
        replyService.delete(id);
        return R.success();
    }

    @Operation(summary = "回复点赞/取消点赞", description = "需登录，toggle")
    @PostMapping("/replies/{id}/like")
    public R<ReplyVO> toggleReplyLike(@PathVariable Long id) {
        return R.success(replyService.toggleLike(id));
    }
}