package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 回复入参
 */
@Data
@Schema(description = "回复入参")
public class ReplyDTO implements Serializable {

    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "冰菓真的值得多刷！")
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 500, message = "回复最长500字")
    private String content;
}