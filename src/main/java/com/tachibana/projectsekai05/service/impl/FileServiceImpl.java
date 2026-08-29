package com.tachibana.projectsekai05.service.impl;

import com.tachibana.projectsekai05.common.exception.BusinessException;
import com.tachibana.projectsekai05.config.FileProperties;
import com.tachibana.projectsekai05.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传服务实现（本地磁盘存储，可访问 URL 为 /uploads/xxx）
 * <p>类型约定：type=avatar 时限制 1MB 并存入 uploads/avatar/ 子目录；其余默认 10MB。
 */
@Service
public class FileServiceImpl implements FileService {

    private static final long MAX_SIZE = 10 * 1024 * 1024L;
    private static final long AVATAR_MAX_SIZE = 1 * 1024 * 1024L;
    private static final String AVATAR_SUB_DIR = "avatar";
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final String uploadDir;

    public FileServiceImpl(FileProperties fileProperties) {
        String dir = fileProperties.getUploadDir();
        if (!dir.endsWith("/") && !dir.endsWith("\\")) {
            dir += "/";
        }
        this.uploadDir = dir;
    }

    @Override
    public Map<String, String> upload(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "请选择要上传的文件");
        }
        boolean avatar = "avatar".equals(type);
        long maxSize = avatar ? AVATAR_MAX_SIZE : MAX_SIZE;
        if (file.getSize() > maxSize) {
            throw new BusinessException(400, avatar ? "头像大小不能超过1MB" : "文件大小不能超过10MB");
        }
        String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = originalFilename.lastIndexOf('.');
        if (dot >= 0) {
            ext = originalFilename.substring(dot + 1).toLowerCase(Locale.ROOT);
        }
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException(400, "仅支持 jpg/jpeg/png/gif/webp 格式图片");
        }

        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath();
            if (avatar) {
                dir = dir.resolve(AVATAR_SUB_DIR);
            }
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new BusinessException(500, "文件保存失败: " + e.getMessage());
        }
        String url = avatar ? "/uploads/avatar/" + filename : "/uploads/" + filename;
        return Map.of("url", url);
    }
}