package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 动漫评论入参
 */
@Data
@Schema(description = "动漫评论入参")
public class AnimeCommentDTO implements Serializable {

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "这部番真的很好看！")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论最长500字")
    private String content;
}