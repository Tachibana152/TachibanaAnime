# 开发记录（完整问答版）

> 项目：ProjectSekai-05（Tachibana 动画世界）
> 记录方式：按时间线完整保留本次开发过程中的提问、决策、操作与排坑记录。
> 说明：A 文档为结构化总结，本文件为完整记录（B），供回溯/课设报告参考。

---

## 阶段一：搭建 Spring Boot 基础框架

**Q：能不能构建一个 SpringBoot 项目基础框架，其中要有基本模块，适配 vue3 前端，有基本的配置类还有基础的功能（比如分页，jwt，签名算法等），包括 springboot 的三个层次。有基础的常量类，以及其他必要的类？**

已确认的技术选型（通过提问确认）：
- **持久层**：MyBatis-Plus（`mybatis-plus-spring-boot4-starter` 3.5.17，官方支持 Boot 4）+ `mybatis-plus-jsqlparser`（分页插件必需）
- **认证**：自定义拦截器 + jjwt（`jjwt-api/impl/gson` 0.12.6，Gson 序列化避开 Jackson3 冲突）
- **接口文档**：springdoc-openapi（`3.0.3` + `spring-boot-jackson2` 桥接，因 swagger-core 尚未迁移 Jackson 3）
- **签名算法**：RSA 非对称签名（SHA256withRSA）

交付内容：
- 三层架构 demo：`AuthController/UserController` → `AuthService/SysUserService` → `SysUserMapper`
- 基础设施：`R<T>`、`PageResult<T>`、`GlobalExceptionHandler`、`ResultCode`、常量类（`CommonConstants/SecurityConstants/RedisConstants`）
- 配置类：`CorsConfig`、`MybatisPlusConfig`、`MetaObjectHandlerConfig`、`RedisConfig`、`OpenApiConfig`、`JwtProperties/SignProperties`
- 工具：`JwtUtil`、`SignUtil`（RSA）、`PasswordUtil`（SHA-256+盐）、`RsaKeyGenerator`
- `sql/init.sql` + 演示账号 admin/test（123456）

**踩坑记录①**：MyBatis-Plus **3.5.17 破坏性变更** —— `IService/ServiceImpl` 从 `com.baomidou.mybatisplus.extension.service` 迁移到 `com.baomidou.mybatisplus.spring.service`，需更新 import。
**踩坑记录②**：springdoc + Boot4 Jackson3 需 `spring-boot-jackson2` 桥接（实测启动正常）。

---

## 阶段二：本地 MySQL / Redis

**Q：可不可以用本地的 mysql 和 redis？**

- 检测到本机 MySQL 8.0.45 运行中（`C:\Program Files\MySQL\MySQL Server 8.0`），Redis 未运行。
- 用户提供：MySQL root 密码 `a359938435`；Redis `102.168.184.128:6379` 密码 `07210721`。
- **排查**：`102.168.184.128` 连接超时，实际可达的是 `192.168.184.128`（笔误），修正。
- 先创建 `mydatabase` + `myuser/secret` 账号并导入 init.sql；后用户改用 `animeairi` 库 + root 账号。
- 关停 `spring.docker.compose`（`enabled: false`），datasource 指向 localhost。

**踩坑记录③**：用户在 IDE 执行 `init.sql` 报 `[3D000][1046] No database selected` —— 脚本头部缺少建库/切库语句。修复：头部加 `CREATE DATABASE IF NOT EXISTS animeairi ...; USE animeairi;`，`INSERT` 改 `INSERT IGNORE` 保证幂等。

**Q：datasource 的 host/port/database 键有问题吗？**
- `host/port/database` 三个键 **Spring Boot 不识别会被忽略**，连接只看 `url`。
- 修正为只用 `url/username/password`，url 指向 `animeairi`。

---

## 阶段三：gitignore 与敏感配置

**Q：帮我写好 gitignore。**
- 重写 `.gitignore`：Maven/Java 产物、IDE（IDEA/Eclipse/VS Code/NetBeans）、OS 文件、日志/密钥、本地配置（`application-local.*`、`application-dev.*` 等）。

**Q：敏感配置放哪里？（选了第一种：移到忽略文件）**
- `application.yml` 改为模板：`spring.profiles.active: local` + 环境变量占位符（`${DB_URL:默认值}`、`${DB_PASSWORD:}` 等）。
- `application-local.yml`（已 gitignore）：真实 MySQL `root/a359938435`、Redis `192.168.184.128/07210721`。
- 优先级：local profile 配置 > 环境变量 > 默认值。

---

## 阶段四：RAG 语料库咨询

**Q：rag 语料库可以读 mysql 数据库里的内容吗？**
- 可：①离线导入（查库→分块→向量化→向量库）②Text2SQL 语义查询 ③混合检索。
- 注：需 embedding + LLM API；本项目曾移除阻塞启动的 `spring-ai-starter-model-deepseek`（缺 API key 报 `DeepSeek API key must be set`）。

---

## 阶段五：前端改造需求（动画资讯站 → Vue3 全栈）

**Q：根据 C:\Users\Tachibana\Desktop\HtmlTest\web课设 改造成 vue3 前端，添加交互内容（登录、管理员管理、高级管理员高级管理、发帖、回复、分页等），并给出对应后端接口。**

已确认决策：
- 前端位置：`ProjectSekai-05/frontend/`
- UI 库：Element Plus
- 权限模型：三级（USER / ADMIN / SUPER_ADMIN）
- 图片：后端托管（classpath 静态 + 外部上传）

**Q：能不能实现发帖，帖子格式参考现有的前端？**（原站论坛区 = 标题 + 多段正文 + 可选来源链接"点击查看更多"）
- `forum_post` 表：`title / content(TEXT) / source_url / status / reject_reason / top / view_count / reply_count`
- 前端正文 `white-space: pre-wrap` 多段渲染。

**Q：能不能添加一个搜索功能，可以搜索相应的动画内容？**
- `GET /api/animes?keyword=` 跨标题/日文名/原作/导演/脚本/制作/简介/正文多字段模糊搜索 + 前端高亮。

**Q：能不能添加审核功能？**
- 发帖需管理员审核：`status 0待审核/1已发布/2已驳回`；`GET /api/admin/posts?status=0` 审核队列；驳回填原因作者可见；管理员/超管发帖直接发布。

**Q：总结现有需求。** → 输出完整需求汇总文档（本文档即延续）。

---

## 阶段六：前端先行构建（Mock 模式）

**Q：先不用写后端具体内容，先构建前端。**
- 前端完成清单：
  - 脚手架：Vite 8 + Vue 3 + Router + Pinia + Axios + Element Plus（中文 locale + 全量图标）
  - `public/uploads/anime/`：素材图片从 `web课设素材` 复制
  - Mock 层：`src/mock/db.js`（18 部动漫 + 帖子 + 回复 + 三级账号种子数据）+ `src/mock/index.js`（接口实现，与后端契约签名一致）
  - 页面：首页(搜索+Tab+分页)、动画详情、登录/注册、论坛、帖子详情+回复、发帖/编辑、我的帖子、动漫管理、帖子管理+审核队列、用户管理
  - 路由守卫（未登录→login；admin 路由按 `meta.roles` 拦截）、角色导航、401 自动跳转
- 验证：`npm run build` ✅；Mock 冒烟测试 **15 项全部通过**（登录/搜索/分页/发帖/审核/驳回/回复/权限403）。

**Q：网站端口是什么？** 前端 `http://localhost:5173`，后端 `http://localhost:8080`（Swagger `/swagger-ui.html`），Redis `192.168.184.128:6379`。

**Q：怎么启动 vue？**
- `cd frontend && npm install && npm run dev`。
- **踩坑记录④**：用户误在项目根目录运行 `npm run dev` 报 `Missing script: "dev"`（根目录是 Maven 项目无 package.json），需进入 `frontend/`。

**Q：发送帖子没有后端数据库现在是怎么实现的？**
- 走前端 **Mock 数据层**：`PostEditView` → `forumApi.createPost` → `USE_MOCK=true` → `mockApi.createPost` → 数据 `posts.unshift(p)` 存入 `src/mock/db.js` 的**内存数组**。
- 限制：**刷新页面数据丢失**（重置为种子数据）；token 为假的 `mock-token-<用户名>`。
- 切换真实后端：`frontend/.env` 设 `VITE_USE_MOCK=false` + vite 代理 `/api`、`/uploads` → `8080`，页面零改动。

---

## 阶段七：后端接口骨架（只写接口，不写实现）

**Q：后端只写对应的接口，先不要写里面的具体实现。然后生成接口文档，其他的先不做改动。**
**Q：做好解耦合以及后面的功能拓展性。**

已确认：接受 Stub 占位实现；允许扩展 `AuthService`/`UserQueryDTO`；并显示后端完整结构。

架构设计（面向接口、解耦）：
```
controller → 薄控制器：参数校验 → 调 Service 接口 → 包 R
service    → 业务接口契约（Java interface）
service/impl → Stub 占位（throw UnsupportedOperationException，保证 DI 可装配）
dto/vo     → 跨层数据契约（实体不泄露到 controller）
security   → @RequireRole 声明式权限（校验集中到后续拦截器）
```

交付内容：
- DTO/VO：`PageQuery`（分页基类）+ 认证/动漫/论坛/回复/审核/用户管理/文件全套契约 DTO
- 常量：`SecurityConstants` 加 `ROLE_SUPER_ADMIN`、新增 `PostConstants`
- `@RequireRole` 注解（类/方法级）
- Service 接口：`AuthService`(扩) / `AnimeService` / `ForumPostService` / `ReplyService` / `AdminPostService` / `UserAdminService` / `FileService`
- Stub 实现 ×7
- Controller：`AnimeController` / `ForumPostController` / `AdminPostController` / `AdminUserController` / `FileController`，`AuthController` 加 register/me
- `docs/api.md`：8 大模块 20 接口，参数表/响应示例/curl 全流程/三级权限表/前端 Mock 对应表

验证：
- `mvnw clean compile` ✅、5 项单测 ✅
- 启动后 `/v3/api-docs` 呈现全部 **20 个端点**；登录返回 200+token；Stub 接口返回 500（预期）

**踩坑记录⑤（重要）**：`@RequireRole` 原 `@Target(ElementType.METHOD)` 却被用在**类上**（AdminPostController/AdminUserController），报 `批注接口不适用于此类型的声明`；该错误使**整轮 javac 注解处理中断**，连锁导致所有 Lombok getter 报"找不到符号"（表象是 Lombok 失效）。修复：`@Target` 改为 `{TYPE, METHOD}`。

**接口契约对齐**（前端随后端微调，`npm run build` ✅）：
- 用户管理路径统一为 `/api/admin/users`（前端 `user.js` 相应修改）
- 回复删除统一为 `/api/forum/posts/replies/{id}`（前端 `forum.js` 修改）
- 文件上传改为 multipart FormData（前端 `fileApi.upload` 构造 FormData）

---

## 阶段八：文档与记录保存

**Q：能保留该询问记录吗？（选 B 完整记录）**
- opencode 会话本身可恢复；另在仓库生成本 `docs/DEVLOG.md` 完整问答记录 + `docs/api.md` 接口文档 + `frontend/README.md` 启动/切换说明。

---

## 附录：最终端口与账号

| 项 | 值 |
|---|---|
| 前端 | `http://localhost:5173` |
| 后端 | `http://localhost:8080`（Swagger `/swagger-ui.html`） |
| MySQL | `localhost:3306`，库 `animeairi`，root/`a359938435`（真实配置在 gitignore 的 `application-local.yml`） |
| Redis | `192.168.184.128:6379` 密码 `07210721` |
| 演示账号 | `admin/123456`（SUPER_ADMIN）、`test/123456`（USER） |

## 附录：待办（下一步：后端实现阶段）

- [ ] 数据库表：`sys_user` 加 role、新建 `anime` / `forum_post` / `forum_reply`，`init.sql` 幂等种子数据
- [ ] entity / mapper：`Anime`、`ForumPost`、`ForumReply`（MyBatis-Plus BaseMapper）
- [ ] 替换 `service/impl/*` Stub 为真实逻辑（分页/搜索/审核/回复/用户管理）
- [ ] `AuthInterceptor` 接入 `@RequireRole` 校验、登录返回 role（`UserInfoVO`）
- [ ] 文件上传：`FileServiceImpl` 本地磁盘 + `/uploads/**` 资源映射；图片从 `frontend/public/uploads/anime/` 迁移到后端静态目录
- [ ] 联调：前端 `.env` 设 `VITE_USE_MOCK=false` 走真实接口