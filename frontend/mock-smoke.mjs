// Mock 层冒烟测试（临时脚本，测试后删除）
import { mockApi } from './src/mock/index.js'

function assert(cond, msg) {
  if (!cond) { console.error('FAIL:', msg); process.exitCode = 1 }
  else console.log('PASS:', msg)
}

async function run() {
  // 登录
  const login = await mockApi.login({ username: 'admin', password: '123456' })
  const adminToken = login.token
  assert(adminToken && login.user.role === 'SUPER_ADMIN', 'admin 登录 + 角色')

  const t = await mockApi.login({ username: 'test', password: '123456' })
  const userToken = t.token

  // 搜索动画
  const s1 = await mockApi.listAnimes({ keyword: '芙莉莲' })
  assert(s1.total >= 1 && s1.records[0].title.includes('芙莉莲'), '搜索「芙莉莲」')
  const s2 = await mockApi.listAnimes({ keyword: '京都' })
  assert(s2.total >= 1, '搜索「京都」(匹配制作公司)')
  const s3 = await mockApi.listAnimes({ keyword: 'KEY' })
  assert(s3.total >= 1, '搜索「KEY」(匹配原作)')

  // 分页
  const page = await mockApi.listAnimes({ pageNum: 1, pageSize: 4 })
  assert(page.records.length === 4 && page.total > 4, '动漫分页')

  // 用户发帖 → 待审核
  const p = await mockApi.createPost(userToken, { title: '测试帖子', content: '这是内容\n第二行', sourceUrl: '' })
  assert(p.status === 0 && !p.autoApproved, '普通用户发帖=待审核')
  const pubList = await mockApi.listPosts({ pageNum: 1, pageSize: 50 })
  assert(!pubList.records.some((x) => x.id === p.id), '待审核帖不在公开列表')

  // 管理员审核通过
  const queue = await mockApi.listAdminPosts(adminToken, { status: 0 })
  assert(queue.records.some((x) => x.id === p.id), '审核队列包含该帖')
  await mockApi.reviewPost(adminToken, p.id, { status: 1, rejectReason: '' })
  const pubList2 = await mockApi.listPosts({ pageNum: 1, pageSize: 50 })
  assert(pubList2.records.some((x) => x.id === p.id), '通过后出现在公开列表')

  // 驳回 + 原因
  const p2 = await mockApi.createPost(userToken, { title: '坏帖子', content: '违规内容', sourceUrl: '' })
  await mockApi.reviewPost(adminToken, p2.id, { status: 2, rejectReason: '内容违规' })
  const mine = await mockApi.myPosts(userToken, { pageNum: 1, pageSize: 50 })
  const rejected = mine.records.find((x) => x.id === p2.id)
  assert(rejected.status === 2 && rejected.rejectReason === '内容违规', '驳回状态+原因')

  // 回复
  const r = await mockApi.createReply(userToken, 1, { content: '沙发' })
  assert(r.id > 0, '回复成功')
  const reps = await mockApi.listReplies(1, { pageNum: 1, pageSize: 10 })
  assert(reps.total >= 4, '回复分页列表')

  // 权限
  let denied = false
  try { await mockApi.listUsers(userToken, {}) } catch (e) { denied = e.code === 403 }
  assert(denied, '普通用户访问用户管理被拒绝(403)')

  let denied2 = false
  try { await mockApi.listAdminPosts(userToken, {}) } catch (e) { denied2 = e.code === 403 }
  assert(denied2, '普通用户访问审核队列被拒绝(403)')

  // 超管用户管理
  const users = await mockApi.listUsers(adminToken, {})
  assert(users.total >= 6, '超管用户分页列表')

  console.log('--- 冒烟测试完成 ---')
}

run().catch((e) => { console.error('UNCAUGHT:', e); process.exitCode = 1 })