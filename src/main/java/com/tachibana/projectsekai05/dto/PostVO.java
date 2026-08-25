package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 论坛帖子出参
 */
@Data
@Schema(description = "论坛帖子")
public class PostVO implements Serializable {

    @Schema(description = "帖子ID")
    private Long id;

    @Schema(description = "作者ID")
    private Long userId;

    @Schema(description = "作者昵称")
    private String username;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文")
    private String content;

    @Schema(description = "来源链接")
    private String sourceUrl;

    @Schema(description = "状态: 0待审核 1已发布 2已驳回")
    private Integer status;

    @Schema(description = "驳回原因")
    private String rejectReason;

    @Schema(description = "是否置顶: 0否 1是")
    private Integer top;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "回复数")
    private Integer replyCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}