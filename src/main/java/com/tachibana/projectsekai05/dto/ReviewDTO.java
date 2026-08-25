package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子审核入参
 */
@Data
@Schema(description = "帖子审核入参")
public class ReviewDTO implements Serializable {

    @Schema(description = "审核结果: 1通过 2驳回", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "审核结果不能为空")
    @Min(value = 1, message = "审核结果取值错误")
    @Max(value = 2, message = "审核结果取值错误")
    private Integer status;

    @Schema(description = "驳回原因（驳回时必填，作者可见）", example = "内容与本站主题无关")
    @Size(max = 200, message = "驳回原因最长200字")
    private String rejectReason;
}