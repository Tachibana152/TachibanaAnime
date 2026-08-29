package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.dto.PostQueryDTO;
import com.tachibana.projectsekai05.dto.PostVO;
import com.tachibana.projectsekai05.dto.UserBriefVO;
import com.tachibana.projectsekai05.dto.UserProfileVO;
import com.tachibana.projectsekai05.dto.UserQueryDTO;
import com.tachibana.projectsekai05.dto.UserVO;
import com.tachibana.projectsekai05.security.NoAuth;
import com.tachibana.projectsekai05.security.RequireRole;
import com.tachibana.projectsekai05.service.AnimeService;
import com.tachibana.projectsekai05.service.ForumPostService;
import com.tachibana.projectsekai05.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户接口（用户主页 / 公开资料）
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;
    private final ForumPostService forumPostService;
    private final AnimeService animeService;

    public UserController(SysUserService sysUserService, ForumPostService forumPostService, AnimeService animeService) {
        this.sysUserService = sysUserService;
        this.forumPostService = forumPostService;
        this.animeService = animeService;
    }

    @Operation(summary = "用户主页", description = "昵称/简介/头像/身份 + 帖子数/贡献动漫数统计")
    @NoAuth
    @GetMapping("/{id}")
    public R<UserProfileVO> profile(@PathVariable Long id) {
        return R.success(sysUserService.profile(id));
    }

    @Operation(summary = "该用户已发布的帖子", description = "用户主页用，置顶优先 + 时间倒序")
    @NoAuth
    @GetMapping("/{id}/posts")
    public R<PageResult<PostVO>> posts(@PathVariable Long id, PostQueryDTO query) {
        return R.success(forumPostService.pageByUser(id, query));
    }

    @Operation(summary = "该用户贡献过的动漫", description = "用户主页用，支持分类/关键词过滤")
    @NoAuth
    @GetMapping("/{id}/animes")
    public R<PageResult<AnimeVO>> animes(@PathVariable Long id, AnimeQueryDTO query) {
        return R.success(animeService.pageByContributor(id, query));
    }

    @Operation(summary = "管理员/超级管理员列表", description = "动漫内容贡献者下拉选项")
    @RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
    @GetMapping("/admins")
    public R<List<UserBriefVO>> admins() {
        return R.success(sysUserService.listAdmins());
    }

    @Operation(summary = "分页查询用户")
    @GetMapping
    public R<PageResult<UserVO>> page(@Valid UserQueryDTO query) {
        return R.success(sysUserService.pageUsers(query));
    }
}