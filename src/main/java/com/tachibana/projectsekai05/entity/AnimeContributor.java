package com.tachibana.projectsekai05.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 动漫内容贡献者（多对多：一个动漫可有多个贡献者）
 */
@Data
@TableName("anime_contributor")
public class AnimeContributor {

    @TableId
    private Long id;

    /** 动漫ID */
    private Long animeId;

    /** 贡献者用户ID */
    private Long userId;

    private LocalDateTime createTime;
}