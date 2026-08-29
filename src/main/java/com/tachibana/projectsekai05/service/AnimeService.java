package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.dto.UserBriefVO;

import java.util.List;

/**
 * 动漫服务
 */
public interface AnimeService {

    /**
     * 分页查询动漫（支持分类过滤 + 关键词搜索）
     */
    PageResult<AnimeVO> pageAnimes(AnimeQueryDTO query);

    /**
     * 动漫详情（浏览量 +1）
     */
    AnimeVO detail(Long id);

    /**
     * 新增动漫（自动将当前操作人加入内容贡献者）
     */
    AnimeVO create(AnimeDTO dto);

    /**
     * 更新动漫（内容贡献者 = 当前操作人 ∪ dto.contributorIds）
     */
    AnimeVO update(Long id, AnimeDTO dto);

    /**
     * 删除动漫（同步清理内容贡献者）
     */
    void delete(Long id);

    /**
     * 某用户贡献过的动漫分页
     */
    PageResult<AnimeVO> pageByContributor(Long userId, AnimeQueryDTO query);

    /**
     * 动漫的内容贡献者列表
     */
    List<UserBriefVO> listContributors(Long animeId);
}