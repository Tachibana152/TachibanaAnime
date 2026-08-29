// 全局常量：角色 / 帖子状态
export const ROLE = {
  USER: 'USER',
  ADMIN: 'ADMIN',
  SUPER_ADMIN: 'SUPER_ADMIN',
}

export const ROLE_LABEL = {
  [ROLE.USER]: '普通用户',
  [ROLE.ADMIN]: '管理员',
  [ROLE.SUPER_ADMIN]: '超级管理员',
}

export const POST_STATUS = {
  PENDING: 0,
  PUBLISHED: 1,
  REJECTED: 2,
}

export const POST_STATUS_LABEL = {
  [POST_STATUS.PENDING]: '待审核',
  [POST_STATUS.PUBLISHED]: '已发布',
  [POST_STATUS.REJECTED]: '已驳回',
}

export const ANIME_CATEGORY = {
  NEW: 'NEW',
  CLASSIC: 'CLASSIC',
}

export const ANIME_CATEGORY_LABEL = {
  [ANIME_CATEGORY.NEW]: '一月新番',
  [ANIME_CATEGORY.CLASSIC]: '经典动画',
}

export const TOKEN_KEY = 'tb_token'
export const USER_KEY = 'tb_user'

// 默认头像（用户未设置头像时的兜底显示）
export const DEFAULT_AVATAR = '/uploads/avatar/default.webp'
// 头像上传大小上限（1MB）
export const AVATAR_MAX_SIZE = 1 * 1024 * 1024