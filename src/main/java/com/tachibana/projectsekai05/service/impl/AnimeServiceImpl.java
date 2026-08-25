package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.service.AnimeService;
import org.springframework.stereotype.Service;

/**
 * 动漫服务实现（占位，待实现）
 */
@Service
public class AnimeServiceImpl implements AnimeService {

    @Override
    public PageResult<AnimeVO> pageAnimes(AnimeQueryDTO query) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public AnimeVO detail(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public AnimeVO create(AnimeDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public AnimeVO update(Long id, AnimeDTO dto) {
        throw new UnsupportedOperationException("接口待实现");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("接口待实现");
    }
}