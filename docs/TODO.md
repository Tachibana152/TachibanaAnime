# 待办清单（TODO）

> 项目：ProjectSekai-05（Tachibana Anime）
> 记录当前未完成 / 计划中的开发事项，供后续开发与课设报告参考
> 更新日期：2026-08-31

---

## 一、验证类（优先）

- [ ] **重启后端**：IntelliJ 重启 Spring Boot（加载 8-29 全部新代码：贡献者/头像/评论点赞/背图字段等）
- [ ] **重跑数据库**：执行 `src/main/resources/sql/init.sql`（DROP+CREATE 幂等），加载新表 `anime_contributor` / `anime_comment` / `comment_like`、`sys_user` 新列（bio/avatar/avatar_pending）、`anime.background`、`forum_reply.like_count`
- [ ] **前端全链路验证**：`cd frontend && npm run dev`，浏览器核对
  - 动漫详情页背景大图 + 半透明文字块 + 底部评论区 + 点赞
  - 富文本标签（彩色/加粗/字号）与 `<script>` 剥离
  - 用户主页 `/user/:id`、设置 `/settings`、头像上传→超管审核
  - 帖子回复点赞、作者名跳主页、站名/页脚/favicon
- [ ] `uploads/` 目录加入 `.gitignore`（上传产物不进版本库）

---

## 二、计划功能：账号单一手机号绑定（含验证码登录）

> 目标：一个手机号只能绑定一个账号（唯一约束），并支持手机号 + 验证码登录。

### 后端
- [ ] `sys_user` 加 `phone VARCHAR(20)` + **唯一索引 `uk_phone`**（保证单一手机号）
- [ ] `SysUser` 实体 / `UserVO` / `UserInfoVO` 加 `phone` 字段（脱敏：前端可做 `138****8888`）
- [ ] **绑定/改绑**：`PUT /api/auth/phone`（登录后）`{ phone }` —— 校验格式（`^1\d{10}$`）+ 唯一性（`DuplicateKeyException` 兜底）+ 非空；刷新 Redis `login:user`
- [ ] **解绑**（可选）：`DELETE /api/auth/phone`（需校验？简单直接解绑）
- [ ] **手机号 + 验证码登录**：`POST /api/auth/phone-login` `{ phone, code }`
  - 验证码：Redis 存 `sms:code:{phone}`（5 分钟有效），**演示阶段用模拟验证码**（如固定 `123456` 或写入日志/返回体），后续可接真实短信服务
  - 未注册手机号自动注册（默认 USER + 随机用户名）或先绑定再登录 —— **二选一，建议先绑定再登录**
- [ ] 登录返回的 `LoginVO.user` 带 `phone`
- [ ] `init.sql` 加 `phone` 列 + 种子；`docs/api.md` 补接口文档

### 前端
- [ ] `UserSettingsView` 加「手机号绑定」区块：显示当前手机号（脱敏）+ 绑定/改绑输入框
- [ ] `LoginView` 加「手机号登录」Tab（手机号 + 验证码，附「获取验证码」按钮与倒计时）
- [ ] Mock 同步（`db.js` users 加 phone、`mockApi` 加绑定/验证码登录/发送验证码）

---

## 三、计划功能：修改密码

> 目标：登录后修改密码，改密后强制下线重新登录。

### 后端
- [ ] `PUT /api/auth/password`（登录后）`{ oldPassword, newPassword }`
- [ ] 校验：
  - 旧密码正确（`PasswordUtil.matches(oldPassword, user.password)`）
  - 新密码强度（复用 `PasswordValidator.isCharacterAndNumber`：字母+数字 ≥8）
  - 新旧密码不同
- [ ] 改密成功后：**删除 Redis `login:token:{id}` + `login:user:{id}` 强制下线**，需重新登录（防旧 token 残留）
- [ ] 密码哈希：`PasswordUtil.encode(newPassword)` 覆盖存储
- [ ] `docs/api.md` 补接口文档

### 前端
- [ ] `UserSettingsView` 加「修改密码」区块：旧密码 / 新密码 / 确认新密码，前端校验一致性与强度
- [ ] 成功后清空表单并提示「已修改，请重新登录」，由用户手动退出或前端 `store.logout()` 跳登录页
- [ ] Mock 同步（`mockApi.updatePassword`）

---

## 四、AI 功能（新增，2026-08-31）

> 今日已落地：langchain4j 接入（Boot4 starter / OpenAI 兼容网关，模型 `deepseek-v4-flash`）、Redis 记忆、Easy RAG、动画自动入向量库。
> 剩余如下（详见 `DEVLOG-2026-08-31.md`）。

### 后端
- [x] **流式聊天接口**（2026-09-02 已实现）：`AIService/dto/ChatDTO.java`（sessionId+message，@NotBlank）+ `AIService/service/AIChatService.java`（`chat` / `chatStream`）+ `service/impl/AIChatServiceImpl.java`（委托 `Assistant`）+ `controller/AIChatController.java`
  - `POST /api/ai/chat` → `R<String>`（同步）✅
  - `POST /api/ai/chat/stream`（`text/event-stream`）→ `Flux<String>` ✅（`Assistant.chatStream` 返回 `Flux<String>`，`langchain4j-reactor` 的 `TokenStreamToFluxAdapter` 自动桥接）
  - 鉴权：不标 `@NoAuth`，由 `AuthInterceptor` 自动拦截（需登录）✅
  - 排坑（2026-09-02 实测）：`base-url` 多余后缀（如 `/responses`）→ 404；模型名不在网关列表（`gpt-5.6-luna`）→ 500；改用 `https://opencode.ai/zen/go/v1` + `deepseek-v4-flash` 后同步/流式全链路验证通过（SSE `data:` 逐帧 50 token）✅
  - 流式错误可见化：`AIChatServiceImpl.chatStream` `onErrorResume` 输出 `[error] ...` 帧（`AiErrorUtil` 收敛友好文案）+ `GlobalExceptionHandler` 同步兜底；前端 `ai.js` 识别 `[error]` 帧 + `AbortController` 90s 超时 + 红色错误气泡 ✅
- [ ] **动画编辑覆盖实测**：`update` 后向量覆盖逻辑已就绪，未跑编辑场景验证
- [x] **`docs/api.md` 补 AI 接口文档**：/api/ai/chat、/api/ai/chat/stream 契约 + SSE 格式

### 前端
- [x] `frontend/src/api/ai.js`：`chat()` 走 axios；`chatStream()` 用 `fetch`（带 `Authorization`）POST + `ReadableStream` 按 `data:` 拆帧回调 `onToken/onDone/onError`（含 Mock 演示打字机）
- [x] AI 聊天页（打字机效果）：`views/AIChatView.vue` + 路由 `/ai` + 顶栏「AI 助手」入口（需登录），会话 ID 可切换（记忆隔离），回车发送/Shift+Enter 换行

### 知识库
- [ ] `rag-docs/` 放入真实知识文档（当前为空，`easyRagIngestor` 启动时按 `doc:{文件名}` 确定性覆盖入库）

---

## 五、其他可选优化（历史遗留）

- [ ] `updateRole` 自我校验精确化：当前为「禁止一切对自己的角色操作」，精确化为「仅禁止降低自己角色」
- [ ] `register` 返回 `UserInfoVO` 补 `id`（契约完整性）
- [ ] 动漫详情页贡献者可编辑时，允许移除「当前操作人自己」的语义确认（当前实现强制并入操作人）
- [ ] 富文本编辑器可升级为所见即所得（现为标签插入 + 提示）

---

## 备注

- 密码相关安全：改密/绑定手机号均需登录态（`AuthInterceptor` 强校验），参数校验优先复用 `PasswordValidator`
- 手机号唯一性由 DB 唯一索引兜底，应用层预检查用于友好提示（参照注册重名处理模式）