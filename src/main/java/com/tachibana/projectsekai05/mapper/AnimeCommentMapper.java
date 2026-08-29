package com.tachibana.projectsekai05.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tachibana.projectsekai05.entity.AnimeComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动漫评论 Mapper
 */
@Mapper
public interface AnimeCommentMapper extends BaseMapper<AnimeComment> {
}