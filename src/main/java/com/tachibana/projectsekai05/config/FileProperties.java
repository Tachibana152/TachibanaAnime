package com.tachibana.projectsekai05.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /** 上传根目录（相对工作目录或绝对路径） */
    private String uploadDir = "uploads/";
}