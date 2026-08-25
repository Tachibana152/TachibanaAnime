package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 回复出参
 */
@Data
@Schema(description = "回复")
public class ReplyVO implements Serializable {

    @Schema(description = "回复ID")
    private Long id;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "回复人ID")
    private Long userId;

    @Schema(description = "回复人昵称")
    private String username;

    @Schema(description = "回复内容")
    private String content;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}