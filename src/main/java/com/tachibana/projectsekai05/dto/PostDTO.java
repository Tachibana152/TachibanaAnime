package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 发帖/编辑入参
 */
@Data
@Schema(description = "发帖/编辑入参")
public class PostDTO implements Serializable {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "看完冰菓的感受")
    @NotBlank(message = "标题不能为空")
    @Size(max = 60, message = "标题最长60字")
    private String title;

    @Schema(description = "正文（多段文字，空行分段）", requiredMode = Schema.RequiredMode.REQUIRED, example = "冰果艺术品般的演出作画音乐……")
    @NotBlank(message = "正文不能为空")
    @Size(max = 5000, message = "正文最长5000字")
    private String content;

    @Schema(description = "来源链接（选填）", example = "https://bgm.tv/blog/358002")
    private String sourceUrl;
}