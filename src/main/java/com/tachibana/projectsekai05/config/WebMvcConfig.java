package com.tachibana.projectsekai05.config;

import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置（注册 JWT 认证拦截器 + 白名单）
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(SecurityConstants.AUTH_PATH_PREFIX)
                .excludePathPatterns(SecurityConstants.WHITE_LIST);
    }
}