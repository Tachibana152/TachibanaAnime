package com.tachibana.projectsekai05.controller;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.security.NoAuth;
import com.tachibana.projectsekai05.security.RequireRole;
import com.tachibana.projectsekai05.service.AnimeService;
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
 * 动漫接口
 */
@Tag(name = "动漫")
@RestController
@RequestMapping("/api/animes")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @NoAuth
    @Operation(summary = "动漫分页列表", description = "支持分类过滤与关键词搜索（标题/日文名/原作/导演/制作/简介/内容）")
    @GetMapping
    public R<PageResult<AnimeVO>> page(AnimeQueryDTO query) {
        return R.success(animeService.pageAnimes(query));
    }

    @NoAuth
    @Operation(summary = "动漫详情", description = "浏览量 +1")
    @GetMapping("/{id}")
    public R<AnimeVO> detail(@PathVariable Long id) {
        return R.success(animeService.detail(id));
    }

    @RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
    @Operation(summary = "新增动漫", description = "需要 ADMIN 或 SUPER_ADMIN")
    @PostMapping
    public R<AnimeVO> create(@Valid @RequestBody AnimeDTO dto) {
        return R.success(animeService.create(dto));
    }

    @RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
    @Operation(summary = "更新动漫", description = "需要 ADMIN 或 SUPER_ADMIN")
    @PutMapping("/{id}")
    public R<AnimeVO> update(@PathVariable Long id, @Valid @RequestBody AnimeDTO dto) {
        return R.success(animeService.update(id, dto));
    }

    @RequireRole({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_SUPER_ADMIN})
    @Operation(summary = "删除动漫", description = "需要 ADMIN 或 SUPER_ADMIN")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        animeService.delete(id);
        return R.success();
    }
}