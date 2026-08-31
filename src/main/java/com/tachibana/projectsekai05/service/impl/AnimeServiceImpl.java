package com.tachibana.projectsekai05.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.AIService.rag.AnimeRagIndexer;
import com.tachibana.projectsekai05.common.result.PageResult;
import com.tachibana.projectsekai05.dto.AnimeDTO;
import com.tachibana.projectsekai05.dto.AnimeQueryDTO;
import com.tachibana.projectsekai05.dto.AnimeVO;
import com.tachibana.projectsekai05.dto.UserBriefVO;
import com.tachibana.projectsekai05.entity.Anime;
import com.tachibana.projectsekai05.entity.AnimeContributor;
import com.tachibana.projectsekai05.mapper.AnimeContributorMapper;
import com.tachibana.projectsekai05.mapper.AnimeMapper;
import com.tachibana.projectsekai05.mapper.SysUserMapper;
import com.tachibana.projectsekai05.security.UserContext;
import com.tachibana.projectsekai05.service.AnimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 动漫服务实现
 */
@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeMapper animeMapper;
    private final AnimeContributorMapper animeContributorMapper;
    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AnimeRagIndexer animeRagIndexer;

    public AnimeServiceImpl(AnimeMapper animeMapper, AnimeContributorMapper animeContributorMapper,
                            SysUserMapper sysUserMapper, RedisTemplate<String, Object> redisTemplate,
                            AnimeRagIndexer animeRagIndexer) {
        this.animeMapper = animeMapper;
        this.animeContributorMapper = animeContributorMapper;
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
        this.animeRagIndexer = animeRagIndexer;
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
        // 内容贡献者：当前操作人 ∪ DTO 指定列表
        List<Long> ids = mergeContributorIds(dto.getContributorIds());
        if (!ids.isEmpty()) {
            syncContributors(anime.getId(), ids);
        }
        // 提交动漫信息到向量库，加入 RAG 知识库
        animeRagIndexer.indexAnime(anime);
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
        // 内容贡献者：当前操作人 ∪ DTO 指定列表（先删后插，保证所有编辑过的人都计入）
        syncContributors(id, mergeContributorIds(dto.getContributorIds()));
        redisTemplate.delete(RedisConstants.CACHE_ANIME_PREFIX + id);
        // 编辑后刷新向量库中的动漫信息（覆盖旧向量）
        animeRagIndexer.indexAnime(animeMapper.selectById(id));
        return toVO(animeMapper.selectById(id));
    }

    @Override
    public void delete(Long id) {
        if (animeMapper.selectById(id) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        animeMapper.deleteById(id);
        animeContributorMapper.delete(new LambdaQueryWrapper<AnimeContributor>()
                .eq(AnimeContributor::getAnimeId, id));
        redisTemplate.delete(RedisConstants.CACHE_ANIME_PREFIX + id);
        // 从向量库移除该动漫的向量，避免删除后仍被 RAG 检索到
        animeRagIndexer.removeAnime(id);
    }

    @Override
    public PageResult<AnimeVO> pageByContributor(Long userId, AnimeQueryDTO query) {
        List<AnimeContributor> rels = animeContributorMapper.selectList(
                new LambdaQueryWrapper<AnimeContributor>().eq(AnimeContributor::getUserId, userId));
        if (rels.isEmpty()) {
            return PageResult.of(List.of(), 0, query.getPageNum(), query.getPageSize());
        }
        List<Long> animeIds = rels.stream().map(AnimeContributor::getAnimeId).distinct().toList();
        LambdaQueryWrapper<Anime> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Anime::getId, animeIds)
                .eq(StringUtils.hasText(query.getCategory()), Anime::getCategory, query.getCategory());
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
    public List<UserBriefVO> listContributors(Long animeId) {
        List<AnimeContributor> rels = animeContributorMapper.selectList(
                new LambdaQueryWrapper<AnimeContributor>().eq(AnimeContributor::getAnimeId, animeId));
        List<Long> userIds = rels.stream().map(AnimeContributor::getUserId).distinct().toList();
        if (userIds.isEmpty()) {
            return List.of();
        }
        return sysUserMapper.selectBatchIds(userIds).stream().map(u -> {
            UserBriefVO vo = new UserBriefVO();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setNickname(u.getNickname());
            vo.setAvatar(u.getAvatar());
            return vo;
        }).toList();
    }

    /**
     * 合并内容贡献者 ID：当前操作人 ∪ 传入列表（去重，忽略空值）
     */
    private List<Long> mergeContributorIds(List<Long> contributorIds) {
        Long current = UserContext.getUserId();
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        if (current != null) {
            set.add(current);
        }
        if (contributorIds != null) {
            for (Long id : contributorIds) {
                if (id != null) {
                    set.add(id);
                }
            }
        }
        return new ArrayList<>(set);
    }

    /**
     * 同步动漫贡献者（先删后插，幂等）
     */
    private void syncContributors(Long animeId, List<Long> userIds) {
        animeContributorMapper.delete(new LambdaQueryWrapper<AnimeContributor>()
                .eq(AnimeContributor::getAnimeId, animeId));
        LocalDateTime now = LocalDateTime.now();
        for (Long userId : userIds) {
            AnimeContributor rel = new AnimeContributor();
            rel.setAnimeId(animeId);
            rel.setUserId(userId);
            rel.setCreateTime(now);
            try {
                animeContributorMapper.insert(rel);
            } catch (Exception ignored) {
                // 唯一键冲突（并发）忽略
            }
        }
    }

    /**
     * DTO -> 实体转换。
     * 约定：同名字段由 BeanUtils 自动拷贝；实体独有字段（id/viewCount/sort/审计字段）不在 DTO 中，不会被覆盖。
     * 新增字段请保持 DTO 与实体同名，否则需在此手动 set。
     */
    private Anime toEntity(AnimeDTO dto) {
        Anime anime = new Anime();
        BeanUtils.copyProperties(dto, anime);
        return anime;
    }

    /**
     * 实体 -> VO 转换。
     * 约定：同名字段由 BeanUtils 自动拷贝（含 viewCount/createTime/background）。
     * 新增字段请保持实体与 VO 同名，否则需在此手动 set。
     */
    private AnimeVO toVO(Anime anime) {
        AnimeVO vo = new AnimeVO();
        BeanUtils.copyProperties(anime, vo);
        return vo;
    }
}
