package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户分页查询")
public class UserQueryDTO extends PageQuery {

    @Schema(description = "用户名模糊查询")
    private String username;

    @Schema(description = "昵称模糊查询")
    private String nickname;
}