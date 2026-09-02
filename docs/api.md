# Tachibana 动画世界 · 后端接口文档

> 项目：ProjectSekai-05（Spring Boot 4 + MyBatis-Plus + JWT + langchain4j）
> 在线文档（springdoc）：`http://localhost:8080/swagger-ui.html` / OpenAPI JSON：`http://localhost:8080/v3/api-docs`
> 说明：核心业务逻辑已实现；AI 聊天接口（同步 + 流式）已实现（见「十三、AI 智能助手与 RAG 知识库」）。
> 依赖：需要 Redis 服务端（含 **RediSearch + RedisJSON** 模块）支撑 RAG 向量库与 AI 会话记忆。

## 一、通用约定

### 1. 统一响应结构 `R<T>`

```json
{ "code": 200, "message": "操作成功", "data": { } }
```

| code | 含义 |
|---|---|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 没有权限访问 |
| 404 | 资源不存在 |
| 500 | 系统内部错误（接口待实现时返回） |

### 2. 分页结构 `PageResult<T>`

```json
{ "records": [], "total": 0, "pageNum": 1, "pageSize": 10 }
```

通用分页参数：`pageNum`（默认 1，≥1）、`pageSize`（默认 10，1~100）。

### 3. 认证方式（JWT Bearer）

除标注「公开」的接口外，均需在请求头携带：

```
Authorization: Bearer <token>
```

登录接口返回 `token` 与 `user.role`，前端持久化后携带。

### 4. 角色权限对照

| 角色 | 常量 | 能力 |
|---|---|---|
| `USER` 普通用户 | `ROLE_USER` | 浏览动漫/论坛、发帖（待审核）、回复、管理自己帖子 |
| `ADMIN` 管理员 | `ROLE_ADMIN` | USER 全部 + 动漫增删改、帖子审核/置顶/删除 |
| `SUPER_ADMIN` 超级管理员 | `ROLE_SUPER_ADMIN` | ADMIN 全部 + 用户管理（禁用/改角色/删除） |

权限由 `@RequireRole` 注解声明在 Controller 上，校验由统一拦截器集中处理（实现阶段接入）。

### 5. 常见字段

- 帖子状态 `status`：`0` 待审核 / `1` 已发布 / `2` 已驳回
- 动漫分类 `category`：`NEW` 一月新番 / `CLASSIC` 经典动画
- 用户状态 `status`：`1` 正常 / `0` 禁用
- 置顶 `top`：`0` 否 / `1` 是

---

## 二、认证接口 `/api/auth`

### 1. 登录 `POST /api/auth/login` 【公开】

请求：
```json
{ "username": "admin", "password": "123456" }
```
响应：
```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": { "id": 1, "username": "admin", "nickname": "管理员", "role": "SUPER_ADMIN", "status": 1 }
  }
}
```

### 2. 注册 `POST /api/auth/register` 【公开】

请求：
```json
{ "username": "user01", "password": "123456", "nickname": "追番萌新" }
```
响应：`data` 为用户信息（默认角色 `USER`）。

### 3. 当前登录用户 `GET /api/auth/me` 【需登录】

响应：`data` 为用户信息 `UserInfoVO {id, username, nickname, role, status, createTime}`。

---

## 三、动漫接口 `/api/animes`

### 1. 动漫分页列表 `GET /api/animes` 【公开】

查询参数：`pageNum`、`pageSize`、`category`（NEW/CLASSIC，可空）、`keyword`（可空）

> `keyword` 搜索范围：标题、日文名、原作、导演、脚本、制作公司、简介、内容、别名（多字段模糊匹配）。

响应：`data` 为 `PageResult<AnimeVO>`。

### 2. 动漫详情 `GET /api/animes/{id}` 【公开】

浏览量 +1。响应：`data` 为 `AnimeVO`。

### 3. 新增动漫 `POST /api/animes` 【ADMIN / SUPER_ADMIN】

请求体 `AnimeDTO`：
```json
{
  "title": "葬送的芙莉莲Ⅱ",
  "titleJp": "葬送のフリーレン 第2期",
  "category": "NEW",
  "cover": "/uploads/anime/Frieren2nd.jpg",
  "background": "/uploads/anime/Frieren2nd_bg.jpg",
  "original": "山田鐘人",
  "director": "北川朋哉",
  "writer": "鈴木智尋",
  "episodes": 12,
  "airDate": "2026年1月16日",
  "airWeekday": "星期五",
  "production": "MADHOUSE",
  "synopsis": "简介…",
  "content": "内容…",
  "storyboard": "分镜…",
  "performance": "演出…",
  "music": "音乐…",
  "charaOriginal": "人物原案…",
  "charaDesign": "人物设定…",
  "seriesComposition": "系列构成…",
  "artDirector": "美术监督…",
  "colorDesign": "色彩设计…",
  "chiefAnimationDirector": "总作画监督…",
  "animationDirector": "作画监督…",
  "photographyDirector": "摄影监督…",
  "planning": "企画…",
  "alias": "别名…",
  "quote": "语录…",
  "contributorIds": [1, 3]
}
```

> 除 `title` 外均选填；`background` 为详情页大图背景；`contributorIds` 为内容贡献者 ID 列表（不传则仅并入当前操作人，详见「十一、9」）。
> **RAG 联动**：新增/编辑成功后，该动漫的全部文本信息（标题/制作阵容/简介/内容/语录等）会自动写入 RAG 向量库（前缀 `anime:{id}`，编辑覆盖旧向量）；删除时清理对应向量。向量库异常不影响动漫 CRUD 主流程（内部捕获并记 WARN）。

### 4. 更新动漫 `PUT /api/animes/{id}` 【ADMIN / SUPER_ADMIN】

请求体同新增。

### 5. 删除动漫 `DELETE /api/animes/{id}` 【ADMIN / SUPER_ADMIN】

---

## 四、论坛帖子接口 `/api/forum/posts`

### 1. 帖子分页列表 `GET /api/forum/posts` 【公开】

查询参数：`pageNum`、`pageSize`、`keyword`（可空）

> 仅返回**已发布**帖子，置顶优先、时间倒序。`keyword` 匹配标题与正文。

### 2. 帖子详情 `GET /api/forum/posts/{id}` 【公开】

仅已发布帖可访问，浏览量 +1。响应 `PostVO {id, userId, username, title, content, sourceUrl, status, rejectReason, top, viewCount, replyCount, createTime}`。

### 3. 我的帖子 `GET /api/forum/posts/mine` 【需登录】

返回当前用户所有帖子（含待审核/已驳回状态与驳回原因）。

### 4. 发帖 `POST /api/forum/posts` 【需登录】

请求体 `PostDTO`：
```json
{ "title": "看完冰菓的感受", "content": "正文（多段）…", "sourceUrl": "https://bgm.tv/blog/358002" }
```
> 普通用户发帖 `status=0`（待审核）；`ADMIN/SUPER_ADMIN` 发帖直接 `status=1` 发布。
> 响应附加字段 `autoApproved` 标识是否直接发布。

### 5. 编辑帖子 `PUT /api/forum/posts/{id}` 【作者本人】

请求体同发帖。待审核/已驳回的帖子编辑后重新进入待审核（`status=0`），并清空驳回原因。

### 6. 删除帖子 `DELETE /api/forum/posts/{id}` 【作者本人 / ADMIN+】

### 7. 置顶 / 取消置顶 `PUT /api/forum/posts/{id}/top` 【ADMIN+】

请求体：
```json
{ "top": 1 }
```

---

## 五、回复接口 `/api/forum/posts`

### 1. 帖子回复列表 `GET /api/forum/posts/{id}/replies` 【公开】

查询参数：`pageNum`、`pageSize`（时间正序）。响应 `PageResult<ReplyVO>`。

### 2. 发表回复 `POST /api/forum/posts/{id}/replies` 【需登录】

请求体：
```json
{ "content": "冰菓真的值得多刷！" }
```

### 3. 删除回复 `DELETE /api/forum/posts/replies/{id}` 【作者本人 / ADMIN+】

---

## 六、帖子审核接口 `/api/admin/posts` 【ADMIN / SUPER_ADMIN】

> 类级 `@RequireRole({ADMIN, SUPER_ADMIN})`，全部接口需要管理员以上。

### 1. 帖子分页列表（按状态）`GET /api/admin/posts`

查询参数：`status`（0 待审核 / 1 已发布 / 2 已驳回，不传=全部）、`pageNum`、`pageSize`。

### 2. 帖子详情（管理员预览）`GET /api/admin/posts/{id}`

可查看未发布帖，用于审核预览。

### 3. 审核帖子 `PUT /api/admin/posts/{id}/review`

请求体 `ReviewDTO`：
```json
{ "status": 1 }
```
或驳回：
```json
{ "status": 2, "rejectReason": "内容与本站主题无关" }
```

---

## 七、用户管理接口 `/api/admin/users` 【SUPER_ADMIN】

> 类级 `@RequireRole(SUPER_ADMIN)`，仅超级管理员可访问。

### 1. 用户分页列表 `GET /api/admin/users`

查询参数：`pageNum`、`pageSize`、`keyword`（用户名/昵称模糊）。响应 `PageResult<UserInfoVO>`（不含密码）。

### 2. 启用 / 禁用用户 `PUT /api/admin/users/{id}/status`

```json
{ "status": 0 }
```
不能操作自己。

### 3. 修改用户角色 `PUT /api/admin/users/{id}/role`

```json
{ "role": "ADMIN" }
```
取值：`USER / ADMIN / SUPER_ADMIN`。不能降低自己的角色。

### 4. 删除用户 `DELETE /api/admin/users/{id}`

不能删除自己。

---

## 八、文件上传接口 `/api/files`

### 上传文件 `POST /api/files/upload` 【需登录】

- `Content-Type: multipart/form-data`
- 表单字段：`file`、`type`（可选；`avatar` 时限制 1MB 并存到 `/uploads/avatar/`，默认 10MB）

响应：
```json
{ "code": 200, "data": { "url": "/uploads/xxx.jpg" } }
```

---

## 九、完整调用流程示例（curl）

```bash
# 1. 登录，获取 token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}' | jq -r '.data.token')

# 2. 带 token 查询动漫分页 + 搜索
curl -s "http://localhost:8080/api/animes?pageNum=1&pageSize=10&keyword=芙莉莲"

# 3. 发帖（管理员直接发布；普通用户进入待审核）
curl -s -X POST http://localhost:8080/api/forum/posts \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"title":"测试帖子","content":"第一段\n\n第二段","sourceUrl":""}'

# 4. 管理员查看待审核队列并审核
curl -s "http://localhost:8080/api/admin/posts?status=0" -H "Authorization: Bearer $TOKEN"
curl -s -X PUT http://localhost:8080/api/admin/posts/1/review \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"status":1}'

# 5. 回复
curl -s -X POST http://localhost:8080/api/forum/posts/1/replies \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"content":"沙发"}'

# 6. 超级管理员用户管理
curl -s "http://localhost:8080/api/admin/users?pageNum=1&pageSize=10" -H "Authorization: Bearer $TOKEN"
curl -s -X PUT http://localhost:8080/api/admin/users/4/status -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"status":0}'
```

---

## 十、前端 Mock 与真实后端对应关系

前端 `frontend/src/api/*.js` 已按本契约实现，当前默认走 Mock（`VITE_USE_MOCK`）。
后端实现就绪后，将 `frontend/.env` 设 `VITE_USE_MOCK=false` 并开启 vite 代理 `/api`、`/uploads` 到 `http://localhost:8080`，页面无需改动。

| 前端模块 | 后端接口前缀 |
|---|---|
| `auth.js` | `/api/auth/*` |
| `anime.js` | `/api/animes/*` |
| `forum.js` | `/api/forum/posts/*`、`/api/admin/posts/*` |
| `user.js` | `/api/admin/users/*`、`/api/files/upload`、`/api/users/*` |
| `ai.js` | `/api/ai/*`（同步 + 流式，见第十三章） |

---

## 十一、用户主页 / 设置 / 头像（新增）

### 1. 用户主页 `GET /api/users/{id}` 【公开】

返回 `UserProfileVO {id, username, nickname, bio, avatar, role, status, createTime, postCount, animeCount}`。

### 2. 该用户已发布的帖子 `GET /api/users/{id}/posts` 【公开】

参数同帖子分页（`pageNum/pageSize`），仅已发布，置顶优先 + 时间倒序。

### 3. 该用户贡献过的动漫 `GET /api/users/{id}/animes` 【公开】

参数同动漫分页（`pageNum/pageSize/category/keyword`），返回该用户作为内容贡献者的动漫。

### 4. 管理员列表 `GET /api/users/admins` 【ADMIN / SUPER_ADMIN】

返回 `[{id, username, nickname, avatar}]`，动漫内容贡献者下拉选项。

### 5. 修改个人资料 `PUT /api/auth/profile` 【需登录】

```json
{ "nickname": "追番萌新", "bio": "热爱动画的追番人" }
```
成功返回最新 `UserInfoVO`（含 `bio/avatar/avatarPending`）。

### 6. 提交头像 `PUT /api/auth/avatar` 【需登录】

```json
{ "avatarUrl": "/uploads/avatar/xxx.webp" }
```
写入 `avatar_pending` 待超级管理员审核，返回当前用户信息。

### 7. 待审核头像列表 `GET /api/admin/users/avatar-audits` 【SUPER_ADMIN】

返回 `avatar_pending` 非空的用户列表。

### 8. 头像审核 `PUT /api/admin/users/avatar-audits/{id}` 【SUPER_ADMIN】

```json
{ "approve": true }
```
`approve=true` 转正（`avatar=avatar_pending`）；`false` 驳回（仅清除待审）。

### 9. 动漫内容贡献者

- `GET /api/animes/{id}/contributors` 【公开】：返回贡献者列表 `[{id, username, nickname, avatar}]`
- 新增/更新动漫（`POST/PUT /api/animes`）请求体可带 `contributorIds: [1,3]`；保存时自动合并当前操作人

---

## 十二、评论与点赞（新增）

### 1. 动漫评论 `GET /api/animes/{id}/comments` 【公开】

分页参数 `pageNum/pageSize`，**时间倒序**（最新在前）。响应 `PageResult<AnimeCommentVO>`：
`{id, animeId, userId, username, content, likeCount, liked, createTime}`。

### 2. 发表动漫评论 `POST /api/animes/{id}/comments` 【需登录】

```json
{ "content": "这部番真的很好看！" }
```

### 3. 删除动漫评论 `DELETE /api/animes/comments/{id}` 【作者本人 / ADMIN+】

### 4. 点赞 / 取消点赞（全站评论通用，toggle）

- 帖子回复：`POST /api/forum/posts/replies/{id}/like` 【需登录】
- 动漫评论：`POST /api/animes/comments/{id}/like` 【需登录】

响应最新评论信息（`likeCount` 增减、`liked` 为当前用户是否已赞）。点赞去重由 `comment_like` 表唯一键保证（再点取消）。

---

## 十三、AI 智能助手与 RAG 知识库（新增）

> 基于 langchain4j 实现：Redis 会话记忆 + Easy RAG 向量检索 + 动漫内容自动入库。
> 两个 `/api/ai/*` 接口已实现（同步 + 流式，流式基于 `Flux<String>` + SSE）。

### 1. RAG 知识库机制（内部说明）

- **向量库**：Redis（服务端需 **RediSearch + RedisJSON** 模块），索引 `embedding-index`，key 前缀 `embedding:`。
- **文档库**：`easy-rag.document-path`（默认 `./rag-docs`）下的知识文档，应用启动时以确定性前缀 `doc:{文件名}` 覆盖写入（重启不重复、不全局清库）。
- **动漫库**：动漫新增/编辑时由 `AnimeRagIndexer` 以 `anime:{id}` 前缀写入（编辑覆盖旧向量），删除时清理 `anime:{id}:*`。
- **嵌入模型**：bge-small-en-v1.5 量化版（本地 ONNX 推理，384 维）。
- **检索参数**：`maxResults=5`、`minScore=0.617`（余弦相似度）。用户提问时先检索相关片段放入上下文，再交由 LLM 回答。
- **会话记忆**：Redis key `chat:memory:{sessionId}`（JSON），每个会话一个滑动窗口（保留最近 20 条），重启不丢；不同会话相互隔离。

### 2. AI 对话（同步）`POST /api/ai/chat` 【需登录】✅ 已实现

请求体 `ChatDTO`：
```json
{ "sessionId": "sess-001", "message": "介绍一下葬送的芙莉莲" }
```

- `sessionId`：会话 ID（`@NotBlank` 必填），同一会话共享上下文记忆。
- 响应：`data` 为 AI 回复文本（`R<String>`）。

### 3. AI 对话（流式）`POST /api/ai/chat/stream` 【需登录】✅ 已实现

- 请求体同上（`ChatDTO`）。
- 响应：`Content-Type: text/event-stream`。
- 实现：`Assistant.chatStream` 返回 `Flux<String>`（`langchain4j-reactor` 的 `TokenStreamToFluxAdapter` 自动桥接），Controller 直接透传，由 MVC 适配为 SSE 逐帧下发。

SSE 帧格式示例：
```
data: 葬

data: 送

data: 的

...
```

- 流结束：服务端关闭 SSE 连接（`data: [DONE]` 不再额外发送）；前端用 `fetch` + `ReadableStream` 拆 `data:` 帧即可。
- 鉴权：未标注 `@NoAuth`，由 `AuthInterceptor` 统一拦截，需携带 `Authorization: Bearer <token>`。