# Tachibana Anime · 前端

动漫资讯 + 论坛的前端项目，基于 **Vue 3 + Vite + Vue Router + Pinia + Axios + Element Plus**。

## 技术栈

| 依赖 | 用途 |
|---|---|
| Vue 3 (`<script setup>`) | 视图层 |
| Vite | 构建 / 开发服务器 |
| Vue Router | 路由 + 权限守卫（按角色拦截） |
| Pinia | 用户状态（token / user / role，localStorage 持久化） |
| Axios | 请求库（拦截器：携带 token、统一解包 `R`、401 跳登录） |
| Element Plus | UI 组件库（中文 locale + 全量图标） |

## 快速启动

```bash
cd frontend
npm install        # 首次需要
npm run dev        # 开发服务器 → http://localhost:5173
npm run build      # 生产构建，产物在 dist/
npm run preview    # 预览构建产物
```

## 数据来源：Mock 模式（默认）

**当前默认运行在 Mock 模式**，不需要后端也能完整演示全部功能。

- 开关：`src/api/client.js` 中 `USE_MOCK = import.meta.env.VITE_USE_MOCK !== 'false'`（未配置环境变量即 `true`）
- Mock 实现：`src/mock/`（`db.js` 内存种子数据 + `index.js` 接口实现，与后端接口契约签名一致）
- 图片：`public/uploads/anime/`（Vite 静态托管，URL 形如 `/uploads/anime/xxx.jpg`）

### ⚠️ Mock 模式的限制
- 发帖、回复等数据保存在**浏览器内存**（`src/mock/db.js` 的 JS 数组）里，**刷新页面即丢失**，恢复为种子数据
- 登录 token 为假的 `mock-token-<用户名>`，仅用于模拟认证与角色权限

### 演示账号
| 账号 | 密码 | 角色 |
|---|---|---|
| `admin` | `123456` | SUPER_ADMIN 超级管理员 |
| `test` | `123456` | USER 普通用户 |

## 切换真实后端

后端接口就绪后（见 `../docs/api.md`），按以下步骤把前端接到 Spring Boot：

### 1. 创建环境变量文件 `frontend/.env`

```ini
VITE_USE_MOCK=false
```

> 或直接修改 `src/api/client.js` 第 6 行的开关。

### 2. 启动后端

```bash
# 在项目根目录（ProjectSekai-05）
.\mvnw.cmd spring-boot:run
```

后端端口 `8080`，需本地 MySQL（`animeairi` 库）与 Redis 可访问，且数据库已初始化（执行 `src/main/resources/sql/init.sql`）。

### 3. Vite 代理（已配置好）

`vite.config.js` 已内置代理，无需改动：

```
/api      → http://localhost:8080
/uploads  → http://localhost:8080
```

### 4. 验证

- 登录 `admin / 123456` 成功、能看到角色
- 发帖 → 进入审核队列 → 管理员通过 → 公开可见（数据此时存入 MySQL `forum_post` 表）
- 动漫列表 / 搜索 / 详情从 MySQL `anime` 表读取

### 切换原理
`src/api/*.js` 每个方法都是 `USE_MOCK ? mockApi.xxx(...) : request.xxx(...)`。切到真实后端后，**只走 axios 分支，页面与组件零改动**；Mock 层保留便于以后无后端演示。

## 目录结构

```
frontend/
├─ public/uploads/anime/     # 静态图片素材（Mock 阶段托管）
├─ src/
│  ├─ api/                   # 接口契约层（auth / anime / forum / user / file）
│  ├─ mock/                  # 内存 Mock：db.js(种子数据) + index.js(接口实现)
│  ├─ stores/user.js         # Pinia 用户状态
│  ├─ router/index.js        # 路由 + 角色守卫
│  ├─ layout/MainLayout.vue  # 主布局（导航按角色显示）
│  ├─ components/            # AnimeCard / PostCard / ReplyItem
│  ├─ views/                 # 页面（首页/详情/登录/论坛/帖子/我的/管理后台×3）
│  └─ constants.js           # 角色 / 帖子状态 / 动漫分类常量
```

## 主要页面

| 路径 | 页面 | 说明 |
|---|---|---|
| `/home` | 首页 | 新番/经典 Tab + 卡片栅格 + 搜索 + 分页 |
| `/anime/:id` | 动画详情 | 海报/元数据/简介/内容 |
| `/login` | 登录/注册 | |
| `/forum` | 论坛 | 帖子列表 + 搜索 + 分页 |
| `/post/:id` | 帖子详情 | 正文 + 回复 + 回复框 |
| `/post/edit` | 发帖/编辑 | 标题 + 多段正文 + 来源链接 |
| `/myposts` | 我的帖子 | 状态标签（待审核/已发布/已驳回） |
| `/admin/anime` | 动漫管理 | ADMIN+：CRUD + 封面上传 |
| `/admin/posts` | 帖子管理/审核 | ADMIN+：待审核队列、通过/驳回、置顶 |
| `/admin/users` | 用户管理 | SUPER_ADMIN：禁用/改角色/删除 |