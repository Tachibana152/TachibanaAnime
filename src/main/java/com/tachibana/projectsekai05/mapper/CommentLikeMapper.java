package com.tachibana.projectsekai05.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tachibana.projectsekai05.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论点赞 Mapper
 */
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
}