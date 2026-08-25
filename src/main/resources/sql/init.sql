-- =============================================================
-- ProjectSekai-05 初始化脚本
-- 数据库: animeairi
-- 用法: 直接在任意客户端执行即可（脚本自动创建并切换到数据库）
-- =============================================================

CREATE DATABASE IF NOT EXISTS animeairi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE animeairi;

CREATE TABLE IF NOT EXISTS sys_user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(128) NOT NULL COMMENT '密码(SHA-256(salt+password))',
    nickname    VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1正常 0禁用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删 1已删',
    create_time DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';

-- 演示账号: admin/123456, test/123456 (密码均为 SHA-256(salt+password))
INSERT IGNORE INTO sys_user (username, password, nickname, status)
VALUES ('admin', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '管理员', 1),
       ('test', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '测试用户', 1);