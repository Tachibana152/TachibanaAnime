package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 头像审核入参
 */
@Data
@Schema(description = "头像审核")
public class AvatarReviewDTO implements Serializable {

    @Schema(description = "是否通过：true 通过（转正），false 驳回", example = "true")
    private boolean approve;
}