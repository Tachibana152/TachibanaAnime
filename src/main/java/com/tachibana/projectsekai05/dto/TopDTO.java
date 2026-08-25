package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 置顶操作入参
 */
@Data
@Schema(description = "置顶操作入参")
public class TopDTO implements Serializable {

    @Schema(description = "是否置顶: 1置顶 0取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "置顶状态不能为空")
    @Min(value = 0, message = "置顶状态取值错误")
    @Max(value = 1, message = "置顶状态取值错误")
    private Integer top;
}