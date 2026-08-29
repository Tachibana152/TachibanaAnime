// =============================================================
// Mock 接口实现（与后端接口契约保持一致）
// 所有函数返回 Promise，成功 resolve 数据，失败 reject {code,message}
// =============================================================
import {
  users, animes, posts, replies, animeComments,
  nextId, paginate, nowStr, buildToken,
  ok, fail, findUserByToken,
} from './db.js'

const ROLE = { USER: 'USER', ADMIN: 'ADMIN', SUPER_ADMIN: 'SUPER_ADMIN' }

function requireAuth(token) {
  const user = findUserByToken(token)
  if (!user) throw Object.assign(new Error('未登录或登录已过期'), { code: 401 })
  return user
}

function requireRole(token, roles) {
  const user = requireAuth(token)
  if (!roles.includes(user.role)) throw Object.assign(new Error('没有权限执行该操作'), { code: 403 })
  return user
}

function delay(ms = 150) {
  return new Promise((r) => setTimeout(r, ms))
}

// 搜索命中：标题/日文名/原作/导演/制作/简介/正文
function animeMatch(a, keyword) {
  if (!keyword) return true
  const k = keyword.toLowerCase()
  const haystack = [a.title, a.titleJp, a.original, a.director, a.writer, a.production, a.synopsis, a.content, a.alias]
    .filter(Boolean)
    .join(' ')
    .toLowerCase()
  return haystack.includes(k)
}

export const mockApi = {
  // ================= 认证 =================
  async login({ username, password }) {
    await delay()
    const user = users.find((u) => u.username === username && u.password === password)
    if (!user) return fail(400, '用户名或密码错误')
    if (user.status !== 1) return fail(400, '账号已被禁用')
    const { password: _p, ...safe } = user
    return ok({ token: buildToken(user.username), user: safe })
  },
  async register({ username, password, nickname }) {
    await delay()
    if (users.some((u) => u.username === username)) return fail(400, '用户名已存在')
    const user = { id: nextId('post') + 1000, username, password, nickname: nickname || username, role: ROLE.USER, status: 1, createTime: nowStr() }
    users.push(user)
    const { password: _p, ...safe } = user
    return ok(safe)
  },
  async me(token) {
    await delay()
    const user = requireAuth(token)
    const { password: _p, ...safe } = user
    return ok(safe)
  },
  async updateProfile(token, { nickname, bio }) {
    await delay()
    const user = requireAuth(token)
    if (nickname !== undefined) {
      if (!String(nickname).trim()) return fail(400, '昵称不能为空')
      user.nickname = String(nickname).trim()
    }
    if (bio !== undefined) user.bio = bio
    const { password: _p, ...safe } = user
    return ok(safe)
  },
  async submitAvatar(token, avatarUrl) {
    await delay()
    const user = requireAuth(token)
    if (!avatarUrl) return fail(400, '头像不能为空')
    user.avatarPending = avatarUrl
    const { password: _p, ...safe } = user
    return ok(safe)
  },

  // ================= 用户主页 =================
  async userProfile(id) {
    await delay()
    const u = users.find((x) => x.id === Number(id))
    if (!u) return fail(404, '用户不存在')
    const postCount = posts.filter((p) => p.userId === u.id && p.status === 1).length
    const animeCount = animes.filter((a) => (a.contributorIds || []).includes(u.id)).length
    const { password: _p, ...safe } = u
    return ok({ ...safe, postCount, animeCount })
  },
  async userPosts(id, { pageNum = 1, pageSize = 10 } = {}) {
    await delay()
    let list = posts.filter((p) => p.userId === Number(id) && p.status === 1)
    list = [...list].sort((a, b) => (b.top - a.top) || (new Date(b.createTime) - new Date(a.createTime)))
    return ok(paginate(list, pageNum, pageSize))
  },
  async userAnimes(id, { pageNum = 1, pageSize = 10, category, keyword } = {}) {
    await delay()
    const uid = Number(id)
    let list = animes.filter((a) => (a.contributorIds || []).includes(uid))
    list = list.filter((a) => !category || a.category === category)
    list = list.filter((a) => animeMatch(a, keyword))
    list = [...list].sort((a, b) => a.sort - b.sort)
    return ok(paginate(list, pageNum, pageSize))
  },
  async animeContributors(animeId) {
    await delay()
    const a = animes.find((x) => x.id === Number(animeId))
    if (!a) return fail(404, '动画不存在')
    const ids = a.contributorIds || []
    return ok(users.filter((u) => ids.includes(u.id)).map(({ password: _p, ...safe }) => safe))
  },
  async listAdmins(token) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    return ok(users.filter((u) => (u.role === ROLE.ADMIN || u.role === ROLE.SUPER_ADMIN) && u.status === 1)
      .map(({ id, username, nickname, avatar }) => ({ id, username, nickname, avatar })))
  },

  // ================= 头像审核（超级管理员） =================
  async listAvatarAudits(token) {
    await delay()
    requireRole(token, [ROLE.SUPER_ADMIN])
    return ok(users.filter((u) => u.avatarPending).map(({ password: _p, ...safe }) => safe))
  },
  async reviewAvatar(token, id, approve) {
    await delay()
    requireRole(token, [ROLE.SUPER_ADMIN])
    const u = users.find((x) => x.id === Number(id))
    if (!u) return fail(404, '用户不存在')
    if (!u.avatarPending) return fail(400, '该用户没有待审核的头像')
    if (approve) u.avatar = u.avatarPending
    u.avatarPending = ''
    return ok(null)
  },

  // ================= 动漫 =================
  async listAnimes({ pageNum = 1, pageSize = 10, category, keyword } = {}) {
    await delay()
    let list = animes.filter((a) => !category || a.category === category)
    list = list.filter((a) => animeMatch(a, keyword))
    list = [...list].sort((a, b) => a.sort - b.sort)
    return ok(paginate(list, pageNum, pageSize))
  },
  async animeDetail(id) {
    await delay()
    const a = animes.find((x) => x.id === Number(id))
    if (!a) return fail(404, '动画不存在')
    a.viewCount += 1
    return ok({ ...a })
  },
  async createAnime(token, form) {
    await delay()
    const user = requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const a = { id: nextId('anime'), category: 'CLASSIC', viewCount: 0, sort: 50, createTime: nowStr(), ...form }
    a.contributorIds = [...new Set([user.id, ...(form.contributorIds || [])])]
    animes.push(a)
    return ok(a)
  },
  async updateAnime(token, id, form) {
    await delay()
    const user = requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const a = animes.find((x) => x.id === Number(id))
    if (!a) return fail(404, '动画不存在')
    Object.assign(a, form, { id: Number(id) })
    a.contributorIds = [...new Set([user.id, ...(form.contributorIds || a.contributorIds || [])])]
    return ok(a)
  },
  async deleteAnime(token, id) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const idx = animes.findIndex((x) => x.id === Number(id))
    if (idx >= 0) animes.splice(idx, 1)
    return ok(null)
  },

  // ================= 论坛 =================
  async listPosts({ pageNum = 1, pageSize = 10, keyword } = {}) {
    await delay()
    let list = posts.filter((p) => p.status === 1)
    if (keyword) {
      const k = keyword.toLowerCase()
      list = list.filter((p) => (p.title + ' ' + p.content).toLowerCase().includes(k))
    }
    list = [...list].sort((a, b) => (b.top - a.top) || (new Date(b.createTime) - new Date(a.createTime)))
    return ok(paginate(list, pageNum, pageSize))
  },
  async postDetail(id) {
    await delay()
    const p = posts.find((x) => x.id === Number(id))
    if (!p || p.status !== 1) return fail(404, '帖子不存在或未发布')
    p.viewCount += 1
    return ok({ ...p })
  },
  async myPosts(token, { pageNum = 1, pageSize = 10 } = {}) {
    await delay()
    const user = requireAuth(token)
    let list = posts.filter((p) => p.userId === user.id)
    list = [...list].sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    return ok(paginate(list, pageNum, pageSize))
  },
  async createPost(token, { title, content, sourceUrl }) {
    await delay()
    const user = requireAuth(token)
    const autoApproved = user.role === ROLE.ADMIN || user.role === ROLE.SUPER_ADMIN
    const p = {
      id: nextId('post'),
      userId: user.id,
      username: user.nickname || user.username,
      title,
      content,
      sourceUrl: sourceUrl || '',
      status: autoApproved ? 1 : 0,
      rejectReason: '',
      top: 0,
      viewCount: 0,
      replyCount: 0,
      createTime: nowStr(),
    }
    posts.unshift(p)
    return ok({ ...p, autoApproved })
  },
  async updatePost(token, id, { title, content, sourceUrl }) {
    await delay()
    const user = requireAuth(token)
    const p = posts.find((x) => x.id === Number(id))
    if (!p) return fail(404, '帖子不存在')
    if (p.userId !== user.id && user.role === ROLE.USER) return fail(403, '只能编辑自己的帖子')
    p.title = title
    p.content = content
    p.sourceUrl = sourceUrl || ''
    if (p.status !== 1) p.status = 0
    p.rejectReason = ''
    return ok({ ...p })
  },
  async deletePost(token, id) {
    await delay()
    const user = requireAuth(token)
    const idx = posts.findIndex((x) => x.id === Number(id))
    if (idx < 0) return fail(404, '帖子不存在')
    const p = posts[idx]
    if (p.userId !== user.id && user.role === ROLE.USER) return fail(403, '没有权限删除该帖子')
    posts.splice(idx, 1)
    return ok(null)
  },
  async toggleTop(token, id, top) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const p = posts.find((x) => x.id === Number(id))
    if (!p) return fail(404, '帖子不存在')
    p.top = top ? 1 : 0
    return ok({ ...p })
  },

  // ================= 审核 =================
  async listAdminPosts(token, { status, pageNum = 1, pageSize = 10 } = {}) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    let list = posts.filter((p) => (status === undefined || p.status === Number(status)))
    list = [...list].sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    return ok(paginate(list, pageNum, pageSize))
  },
  async adminPostDetail(token, id) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const p = posts.find((x) => x.id === Number(id))
    if (!p) return fail(404, '帖子不存在')
    return ok({ ...p })
  },
  async reviewPost(token, id, { status, rejectReason }) {
    await delay()
    requireRole(token, [ROLE.ADMIN, ROLE.SUPER_ADMIN])
    const p = posts.find((x) => x.id === Number(id))
    if (!p) return fail(404, '帖子不存在')
    p.status = Number(status)
    p.rejectReason = status === 2 ? rejectReason || '不符合发布规范' : ''
    return ok({ ...p })
  },

  // ================= 回复 =================
  async listReplies(postId, { pageNum = 1, pageSize = 10 } = {}) {
    await delay()
    const me = findUserByToken(localStorage.getItem('tb_token') || '')
    let list = replies.filter((r) => r.postId === Number(postId))
    list = [...list].sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
    list = list.map((r) => ({ ...r, liked: me ? (r.likedUsers || []).includes(me.id) : false }))
    return ok(paginate(list, pageNum, pageSize))
  },
  async createReply(token, postId, { content }) {
    await delay()
    const user = requireAuth(token)
    const p = posts.find((x) => x.id === Number(postId))
    if (!p) return fail(404, '帖子不存在')
    const r = { id: nextId('reply'), postId: Number(postId), userId: user.id, username: user.nickname || user.username, content, likeCount: 0, likedUsers: [], liked: false, createTime: nowStr() }
    replies.push(r)
    p.replyCount += 1
    return ok(r)
  },
  async toggleReplyLike(token, id) {
    await delay()
    const user = requireAuth(token)
    const r = replies.find((x) => x.id === Number(id))
    if (!r) return fail(404, '回复不存在')
    r.likedUsers = r.likedUsers || []
    const idx = r.likedUsers.indexOf(user.id)
    if (idx >= 0) {
      r.likedUsers.splice(idx, 1)
      r.likeCount = Math.max(0, (r.likeCount || 1) - 1)
      r.liked = false
    } else {
      r.likedUsers.push(user.id)
      r.likeCount = (r.likeCount || 0) + 1
      r.liked = true
    }
    return ok({ ...r })
  },
  async deleteReply(token, id) {
    await delay()
    const user = requireAuth(token)
    const idx = replies.findIndex((r) => r.id === Number(id))
    if (idx < 0) return fail(404, '回复不存在')
    const r = replies[idx]
    if (r.userId !== user.id && user.role === ROLE.USER) return fail(403, '没有权限删除该回复')
    replies.splice(idx, 1)
    const p = posts.find((x) => x.id === r.postId)
    if (p) p.replyCount = Math.max(0, p.replyCount - 1)
    return ok(null)
  },

  // ================= 动漫评论 =================
  async listAnimeComments(animeId, { pageNum = 1, pageSize = 10 } = {}) {
    await delay()
    const me = findUserByToken(localStorage.getItem('tb_token') || '')
    let list = animeComments.filter((c) => c.animeId === Number(animeId))
    list = [...list].sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    list = list.map((c) => ({ ...c, liked: me ? (c.likedUsers || []).includes(me.id) : false }))
    return ok(paginate(list, pageNum, pageSize))
  },
  async createAnimeComment(token, animeId, { content }) {
    await delay()
    const user = requireAuth(token)
    if (!animes.some((x) => x.id === Number(animeId))) return fail(404, '动画不存在')
    const c = { id: nextId('reply') + 1000, animeId: Number(animeId), userId: user.id, username: user.nickname || user.username, content, likeCount: 0, likedUsers: [], liked: false, createTime: nowStr() }
    animeComments.unshift(c)
    return ok(c)
  },
  async deleteAnimeComment(token, id) {
    await delay()
    const user = requireAuth(token)
    const idx = animeComments.findIndex((c) => c.id === Number(id))
    if (idx < 0) return fail(404, '评论不存在')
    const c = animeComments[idx]
    if (c.userId !== user.id && user.role === ROLE.USER) return fail(403, '没有权限删除该评论')
    animeComments.splice(idx, 1)
    return ok(null)
  },
  async toggleAnimeCommentLike(token, id) {
    await delay()
    const user = requireAuth(token)
    const c = animeComments.find((x) => x.id === Number(id))
    if (!c) return fail(404, '评论不存在')
    c.likedUsers = c.likedUsers || []
    const idx = c.likedUsers.indexOf(user.id)
    if (idx >= 0) {
      c.likedUsers.splice(idx, 1)
      c.likeCount = Math.max(0, (c.likeCount || 1) - 1)
      c.liked = false
    } else {
      c.likedUsers.push(user.id)
      c.likeCount = (c.likeCount || 0) + 1
      c.liked = true
    }
    return ok({ ...c })
  },

  // ================= 用户管理 =================
  async listUsers(token, { pageNum = 1, pageSize = 10, keyword } = {}) {
    await delay()
    requireRole(token, [ROLE.SUPER_ADMIN])
    let list = [...users]
    if (keyword) {
      const k = keyword.toLowerCase()
      list = list.filter((u) => u.username.toLowerCase().includes(k) || (u.nickname || '').toLowerCase().includes(k))
    }
    list = list.map(({ password, ...safe }) => safe)
    return ok(paginate(list, pageNum, pageSize))
  },
  async updateUserStatus(token, id, status) {
    await delay()
    const admin = requireRole(token, [ROLE.SUPER_ADMIN])
    const u = users.find((x) => x.id === Number(id))
    if (!u) return fail(404, '用户不存在')
    if (u.id === admin.id) return fail(400, '不能禁用自己的账号')
    u.status = Number(status)
    return ok(null)
  },
  async updateUserRole(token, id, role) {
    await delay()
    const admin = requireRole(token, [ROLE.SUPER_ADMIN])
    const u = users.find((x) => x.id === Number(id))
    if (!u) return fail(404, '用户不存在')
    if (u.id === admin.id && role !== ROLE.SUPER_ADMIN) return fail(400, '不能降低自己的角色')
    u.role = role
    return ok(null)
  },
  async deleteUser(token, id) {
    await delay()
    const admin = requireRole(token, [ROLE.SUPER_ADMIN])
    if (Number(id) === admin.id) return fail(400, '不能删除自己')
    const idx = users.findIndex((x) => x.id === Number(id))
    if (idx < 0) return fail(404, '用户不存在')
    users.splice(idx, 1)
    return ok(null)
  },

  // ================= 文件 =================
  async upload(token, file, type) {
    await delay()
    requireAuth(token)
    const name = encodeURIComponent(file?.name || 'cover.jpg')
    const dir = type === 'avatar' ? '/uploads/avatar/' : '/uploads/anime/'
    return ok({ url: `${dir}${name}` })
  },
}