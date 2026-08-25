package com.tachibana.projectsekai05.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 用户分页查询入参
 */
@Data
public class UserQueryDTO {

    @Min(value = 1, message = "页码最小为1")
    private long pageNum = 1;

    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private long pageSize = 10;

    /** 用户名模糊查询 */
    private String username;

    /** 昵称模糊查询 */
    private String nickname;
}