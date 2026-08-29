package com.tachibana.projectsekai05.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传服务（存储抽象：当前为本地磁盘，后续可替换为 OSS/MinIO 而不改动调用方）
 */
public interface FileService {

    /**
     * 上传文件，返回可访问的 URL
     *
     * @param file 上传的文件
     * @param type 用途类型：avatar（1MB 限制，存 uploads/avatar/）或 null（默认 10MB）
     * @return 形如 {url: "/uploads/xxx.jpg"}
     */
    Map<String, String> upload(MultipartFile file, String type);
}