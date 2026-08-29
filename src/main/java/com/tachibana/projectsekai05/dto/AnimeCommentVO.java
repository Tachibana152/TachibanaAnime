package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动漫评论出参
 */
@Data
@Schema(description = "动漫评论")
public class AnimeCommentVO implements Serializable {

    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "动漫ID")
    private Long animeId;

    @Schema(description = "评论人ID")
    private Long userId;

    @Schema(description = "评论人昵称")
    private String username;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "当前用户是否已点赞")
    private Boolean liked;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}