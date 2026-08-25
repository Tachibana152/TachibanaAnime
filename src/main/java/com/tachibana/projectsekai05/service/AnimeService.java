package com.tachibana.projectsekai05.service;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;

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
     * 新增动漫
     */
    AnimeVO create(AnimeDTO dto);

    /**
     * 更新动漫
     */
    AnimeVO update(Long id, AnimeDTO dto);

    /**
     * 删除动漫
     */
    void delete(Long id);
}