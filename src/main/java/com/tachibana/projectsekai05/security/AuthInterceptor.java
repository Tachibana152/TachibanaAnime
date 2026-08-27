package com.tachibana.projectsekai05.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tachibana.projectsekai05.common.constant.RedisConstants;
import com.tachibana.projectsekai05.common.constant.SecurityConstants;
import com.tachibana.projectsekai05.common.enums.ResultCode;
import com.tachibana.projectsekai05.common.result.R;
import com.tachibana.projectsekai05.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * JWT 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${security.redis-session.enabled:true}")
    private boolean redisSessionEnabled;

    public AuthInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        if (handlerMethod.hasMethodAnnotation(NoAuth.class)) {
            return true;
        }

        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (!StringUtils.hasText(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return writeUnauthorized(response);
        }
        String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());
        if (!jwtUtil.isValid(token)) {
            return writeUnauthorized(response);
        }

        Claims claims = jwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.getSubject());
        if (redisSessionEnabled) {
            Object stored = redisTemplate.opsForValue().get(RedisConstants.TOKEN_PREFIX + userId);
            if (stored == null || !token.equals(String.valueOf(stored))) {
                return writeUnauthorized(response);
            }
        }
        UserContext.set("userId", userId);
        UserContext.set("username", claims.get("username", String.class));
        UserContext.set("role", claims.get("role", String.class));

        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }
        if (requireRole != null) {
            String currentRole = UserContext.getRole();
            boolean allowed = Arrays.asList(requireRole.value()).contains(currentRole);
            if (!allowed) {
                return writeForbidden(response);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private boolean writeForbidden(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        R<Void> body = R.fail(ResultCode.FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(body));
        return false;
    }

    private boolean writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        R<Void> body = R.fail(ResultCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(body));
        return false;
    }
}