# ProjectSekai-05 · Tachibana Anime

动漫资讯 + 论坛 + AI 智能助手的全栈课程设计项目，前后端分离。

- 后端：Spring Boot 4.1.1 + MyBatis-Plus + MySQL + Redis + langchain4j
- 前端：Vue 3 + Vite + Element Plus
- 语言：Java 21 / Node 18+

---

## 功能特性

### 核心业务
- **三级权限体系**：`USER` / `ADMIN` / `SUPER_ADMIN`，`@RequireRole` 声明式校验 + JWT/Redis 双会话校验，改角色即时生效
- **动漫库**：分类浏览、9 字段模糊搜索（含别名）、详情页背景大图 + 富文本正文、评论 + 点赞、内容贡献者多对多
- **论坛**：发帖/编辑/我的帖子、管理员审核（通过/驳回）、置顶、回复 + 点赞
- **用户体系**：注册/登录、用户主页、资料与头像修改（头像超管审核）、三级管理后台
- **文件上传**：本地磁盘 + `/uploads/**` 静态映射，10MB 限制，头像 1MB

### AI 智能助手（langchain4j）
- **接入模型**：`gpt-5.6-luna`（OpenAI 兼容），支持同步与流式对话
- **会话记忆**：滑动窗口（20 条）+ Redis 持久化，重启不丢
- **Easy RAG**：bge-small-en-v1.5 本地嵌入 → Redis 向量库；动漫新增/编辑自动入库、删除清理，启动时加载 `rag-docs/` 知识文档

---

## 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Spring Boot 4.1.1、MyBatis-Plus（Boot4 starter）、MySQL 8、Spring Data Redis、JWT（jjwt + RSA 签名）、langchain4j（open-ai / easy-rag / community-redis / reactor）、springdoc-openapi |
| 前端 | Vue 3（`<script setup>`）、Vite、Vue Router、Pinia、Axios、Element Plus、DOMPurify |
| 基础设施 | Redis（会话/缓存/浏览量计数/向量库）、Maven Wrapper |

---

## 目录结构

```
ProjectSekai-05/
├─ src/main/java/com/tachibana/projectsekai05/
│  ├─ controller/      # REST 接口
│  ├─ service/         # 业务接口 + impl 实现
│  ├─ mapper/          # MyBatis-Plus Mapper
│  ├─ entity/ dto/ vo/ # 数据模型与传输对象
│  ├─ security/        # @RequireRole / @NoAuth / AuthInterceptor
│  ├─ config/          # Redis / WebMvc / 序列化等配置
│  ├─ common/          # R<T>、PageResult、全局异常、常量、工具
│  └─ AIService/       # AI 助手：Assistant、记忆、RAG
├─ src/main/resources/sql/init.sql   # 数据库初始化（幂等）
├─ frontend/           # Vue 3 前端（见 frontend/README.md）
├─ uploads/            # 上传产物（图片/头像）
├─ rag-docs/           # AI 知识库文档目录
└─ docs/               # api.md / DEPLOY.md / TODO.md / DEVLOG*.md
```

---

## 快速开始

### 前置依赖

- JDK 21、Maven（或用内置 `mvnw`）
- Node 18+ / npm
- MySQL 8
- Redis（**AI 功能需要 RediSearch + RedisJSON 模块**，建议用 `redis/redis-stack-server` 镜像；纯业务功能普通 Redis 即可）

### 1. 初始化数据库

执行 `src/main/resources/sql/init.sql`（脚本自动 `DROP + CREATE + INSERT`，可重复执行），建库建表并写入演示账号与动漫种子数据。

### 2. 配置后端

`application.yml` 是模板（环境变量占位符）。真实连接信息写到 **gitignore 的** `application-local.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/animeairi?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 你的数据库密码
  data:
    redis:
      host: 192.168.184.128
      port: 6379
      password: "07210721"   # 注意：前导零密码必须加引号
```

> 生产环境推荐用环境变量覆盖（`DB_URL` / `DB_PASSWORD` / `REDIS_HOST` 等），密钥不进代码库。

### 3. 启动后端

```bash
.\mvnw.cmd spring-boot:run        # → http://localhost:8080
```

- Swagger 接口文档：`http://localhost:8080/swagger-ui.html`
- 完整接口契约见 `docs/api.md`

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev                       # → http://localhost:5173
```

默认走 Mock 模式（无需后端即可演示）；接真实后端时在 `frontend/.env` 写 `VITE_USE_MOCK=false`（Vite 已内置 `/api`、`/uploads` 代理）。详见 `frontend/README.md`。

### 5. 配置 AI 助手（可选）

在 `application-local.yml` 中配置：

```yaml
langchain4j:
  open-ai:
    api-key: 你的API密钥
    model-name: gpt-5.6-luna
    streaming-chat-model: true
    max-tokens: 128000
    temperature: 0.7

easy-rag:
  document-path: ./rag-docs        # 启动时自动向量化入库
  redis:
    host: 192.168.184.128
    port: 6379
    password: "07210721"
```

---

## 演示账号

| 账号 | 密码 | 角色 |
|---|---|---|
| `admin` | `123456` | SUPER_ADMIN 超级管理员 |
| `tachibana` | `123456` | ADMIN 管理员 |
| `test` | `123456` | USER 普通用户 |

> 上线前务必修改默认密码与 `application.yml` 中的 `jwt.secret`。

---

## 文档索引

| 文档 | 说明 |
|---|---|
| `docs/api.md` | 后端接口契约（统一响应/分页/鉴权/权限表 + 全接口） |
| `docs/DEPLOY.md` | 云服务器部署指南（Nginx + systemd + HTTPS） |
| `docs/TODO.md` | 待办清单（验证项/计划功能/优化项） |
| `docs/DEVLOG.md` | 开发记录（阶段一~八，完整问答版） |
| `docs/DEVLOG-2026-*.md` | 分日开发记录与排坑记录 |
| `frontend/README.md` | 前端说明（启动/Mock/切真实后端） |

---

## 当前状态与路线图

- ✅ 核心业务（认证/动漫/论坛/审核/用户管理/评论点赞/文件上传）已完成
- ✅ AI 基础能力（模型接入、Redis 会话记忆、Easy RAG、动画自动入向量库）已落地
- 🚧 AI 流式聊天接口实现中（`Flux<String>` + SSE 方案）
- ⏳ 待办：手机号绑定与验证码登录、修改密码、RAG 知识文档填充、浏览器全链路验证等（详见 `docs/TODO.md`）

---

## License

MIT