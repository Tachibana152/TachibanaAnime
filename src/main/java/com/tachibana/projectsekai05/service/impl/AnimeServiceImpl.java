package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.entity.Anime;
import com.tachibana.projectsekai05.mapper.AnimeMapper;
import com.tachibana.projectsekai05.service.AnimeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 动漫服务实现
 */
@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeMapper animeMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public AnimeServiceImpl(AnimeMapper animeMapper, RedisTemplate<String, Object> redisTemplate) {
        this.animeMapper = animeMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageResult<AnimeVO> pageAnimes(AnimeQueryDTO query) {
        LambdaQueryWrapper<Anime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(query.getCategory()), Anime::getCategory, query.getCategory());
        String keyword = query.getKeyword();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Anime::getTitle, keyword)
                    .or().like(Anime::getTitleJp, keyword)
                    .or().like(Anime::getOriginal, keyword)
                    .or().like(Anime::getDirector, keyword)
                    .or().like(Anime::getWriter, keyword)
                    .or().like(Anime::getProduction, keyword)
                    .or().like(Anime::getSynopsis, keyword)
                    .or().like(Anime::getContent, keyword)
                    .or().like(Anime::getAlias, keyword));
        }
        wrapper.orderByAsc(Anime::getSort);
        IPage<Anime> page = animeMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        List<AnimeVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public AnimeVO detail(Long id) {
        Object cached = redisTemplate.opsForValue().get(RedisConstants.CACHE_ANIME_PREFIX + id);
        AnimeVO vo;
        if (cached instanceof AnimeVO animeVO) {
            vo = animeVO;
        } else {
            Anime anime = animeMapper.selectById(id);
            if (anime == null) {
                throw new BusinessException(ResultCode.NOT_FOUND);
            }
            vo = toVO(anime);
            redisTemplate.opsForValue().set(RedisConstants.CACHE_ANIME_PREFIX + id, vo,
                    RedisConstants.CACHE_ANIME_EXPIRE, TimeUnit.SECONDS);
        }
        redisTemplate.opsForValue().increment(RedisConstants.COUNT_ANIME_PREFIX + id);
        animeMapper.update(null, new LambdaUpdateWrapper<Anime>()
                .eq(Anime::getId, id)
                .setSql("view_count = view_count + 1"));
        vo.setViewCount(vo.getViewCount() == null ? 1L : vo.getViewCount() + 1L);
        return vo;
    }

    @Override
    public AnimeVO create(AnimeDTO dto) {
        Anime anime = toEntity(dto);
        LocalDateTime now = LocalDateTime.now();
        anime.setDeleted(0);
        anime.setCreateTime(now);
        anime.setUpdateTime(now);
        anime.setViewCount(0L);
        anime.setSort(dto.getTitle() == null ? 0 : dto.getTitle().hashCode() % 100);
        animeMapper.insert(anime);
        return toVO(anime);
    }

    @Override
    public AnimeVO update(Long id, AnimeDTO dto) {
        Anime existing = animeMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        Anime anime = toEntity(dto);
        anime.setId(id);
        anime.setUpdateTime(LocalDateTime.now());
        animeMapper.updateById(anime);
        redisTemplate.delete(RedisConstants.CACHE_ANIME_PREFIX + id);
        return toVO(animeMapper.selectById(id));
    }

    @Override
    public void delete(Long id) {
        if (animeMapper.selectById(id) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        animeMapper.deleteById(id);
        redisTemplate.delete(RedisConstants.CACHE_ANIME_PREFIX + id);
    }

    private Anime toEntity(AnimeDTO dto) {
        Anime anime = new Anime();
        anime.setTitle(dto.getTitle());
        anime.setTitleJp(dto.getTitleJp());
        anime.setCategory(dto.getCategory());
        anime.setCover(dto.getCover());
        anime.setBackground(dto.getBackground());
        anime.setOriginal(dto.getOriginal());
        anime.setDirector(dto.getDirector());
        anime.setWriter(dto.getWriter());
        anime.setEpisodes(dto.getEpisodes());
        anime.setAirDate(dto.getAirDate());
        anime.setAirWeekday(dto.getAirWeekday());
        anime.setProduction(dto.getProduction());
        anime.setSynopsis(dto.getSynopsis());
        anime.setContent(dto.getContent());
        anime.setStoryboard(dto.getStoryboard());
        anime.setPerformance(dto.getPerformance());
        anime.setMusic(dto.getMusic());
        anime.setCharaOriginal(dto.getCharaOriginal());
        anime.setCharaDesign(dto.getCharaDesign());
        anime.setSeriesComposition(dto.getSeriesComposition());
        anime.setArtDirector(dto.getArtDirector());
        anime.setColorDesign(dto.getColorDesign());
        anime.setChiefAnimationDirector(dto.getChiefAnimationDirector());
        anime.setAnimationDirector(dto.getAnimationDirector());
        anime.setPhotographyDirector(dto.getPhotographyDirector());
        anime.setPlanning(dto.getPlanning());
        anime.setAlias(dto.getAlias());
        anime.setQuote(dto.getQuote());
        return anime;
    }

    private AnimeVO toVO(Anime anime) {
        AnimeVO vo = new AnimeVO();
        vo.setId(anime.getId());
        vo.setTitle(anime.getTitle());
        vo.setTitleJp(anime.getTitleJp());
        vo.setCategory(anime.getCategory());
        vo.setCover(anime.getCover());
        vo.setBackground(anime.getBackground());
        vo.setOriginal(anime.getOriginal());
        vo.setDirector(anime.getDirector());
        vo.setWriter(anime.getWriter());
        vo.setEpisodes(anime.getEpisodes());
        vo.setAirDate(anime.getAirDate());
        vo.setAirWeekday(anime.getAirWeekday());
        vo.setProduction(anime.getProduction());
        vo.setSynopsis(anime.getSynopsis());
        vo.setContent(anime.getContent());
        vo.setStoryboard(anime.getStoryboard());
        vo.setPerformance(anime.getPerformance());
        vo.setMusic(anime.getMusic());
        vo.setCharaOriginal(anime.getCharaOriginal());
        vo.setCharaDesign(anime.getCharaDesign());
        vo.setSeriesComposition(anime.getSeriesComposition());
        vo.setArtDirector(anime.getArtDirector());
        vo.setColorDesign(anime.getColorDesign());
        vo.setChiefAnimationDirector(anime.getChiefAnimationDirector());
        vo.setAnimationDirector(anime.getAnimationDirector());
        vo.setPhotographyDirector(anime.getPhotographyDirector());
        vo.setPlanning(anime.getPlanning());
        vo.setAlias(anime.getAlias());
        vo.setQuote(anime.getQuote());
        vo.setViewCount(anime.getViewCount());
        vo.setCreateTime(anime.getCreateTime());
        return vo;
    }
}
