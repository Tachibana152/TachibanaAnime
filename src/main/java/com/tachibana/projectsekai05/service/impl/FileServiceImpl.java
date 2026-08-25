package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传服务实现（占位，待实现）
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public Map<String, String> upload(MultipartFile file) {
        throw new UnsupportedOperationException("接口待实现");
    }
}