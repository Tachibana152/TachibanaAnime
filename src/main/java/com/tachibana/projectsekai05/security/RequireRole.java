package com.tachibana.projectsekai05.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色权限注解：标注在 Controller 类或方法上，要求当前用户角色属于允许集合
 * <p>类上标注作用于该类所有接口；方法上标注覆盖类级。权限校验由统一的拦截器/AOP 集中处理（实现阶段接入）。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {

    /**
     * 允许访问的角色列表，如 {@code @RequireRole({ADMIN, SUPER_ADMIN})}
     */
    String[] value();
}