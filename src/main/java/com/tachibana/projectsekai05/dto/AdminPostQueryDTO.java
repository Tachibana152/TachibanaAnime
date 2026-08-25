package com.tachibana.projectsekai05.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员帖子查询入参（审核队列）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员帖子查询")
public class AdminPostQueryDTO extends PageQuery {

    @Schema(description = "帖子状态: 0待审核 1已发布 2已驳回（不传=全部）", example = "0")
    private Integer status;
}