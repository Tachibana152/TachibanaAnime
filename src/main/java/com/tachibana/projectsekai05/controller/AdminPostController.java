package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.AdminPostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.ReviewDTO;
import com.tachibana.projectsekai05.security.RequireRole;
import com.tachibana.projectsekai05.service.AdminPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子审核接口（管理员）
 */
@Tag(name = "帖子审核")
@RestController
@RequestMapping("/api/admin/posts")
@RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
public class AdminPostController {

    private final AdminPostService adminPostService;

    public AdminPostController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @Operation(summary = "帖子分页列表（按状态）", description = "status=0 待审核队列；不传返回全部")
    @GetMapping
    public R<PageResult<PostVO>> page(AdminPostQueryDTO query) {
        return R.success(adminPostService.pagePosts(query));
    }

    @Operation(summary = "帖子详情（管理员预览）", description = "可查看未发布帖")
    @GetMapping("/{id}")
    public R<PostVO> detail(@PathVariable Long id) {
        return R.success(adminPostService.detail(id));
    }

    @Operation(summary = "审核帖子", description = "1通过 2驳回（驳回需填原因）")
    @PutMapping("/{id}/review")
    public R<PostVO> review(@PathVariable Long id, @Valid @RequestBody ReviewDTO dto) {
        return R.success(adminPostService.review(id, dto));
    }
}