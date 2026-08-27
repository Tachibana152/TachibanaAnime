package com.tachibana.projectsekai05.config;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web MVC 配置（注册 JWT 认证拦截器 + 白名单 + /uploads 静态资源映射）
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final FileProperties fileProperties;

    public WebMvcConfig(AuthInterceptor authInterceptor, FileProperties fileProperties) {
        this.authInterceptor = authInterceptor;
        this.fileProperties = fileProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(SecurityConstants.AUTH_PATH_PREFIX)
                .excludePathPatterns(SecurityConstants.WHITE_LIST);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = fileProperties.getUploadDir();
        if (!uploadDir.endsWith("/") && !uploadDir.endsWith("\\")) {
            uploadDir += "/";
        }
        String absolute = Paths.get(uploadDir).toAbsolutePath().toString().replace("\\", "/");
        if (!absolute.endsWith("/")) {
            absolute += "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolute);
    }
}