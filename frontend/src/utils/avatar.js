import { DEFAULT_AVATAR } from '@/constants'

// 用户头像兜底：无头像时显示默认头像
export function avatarUrl(user) {
  return user?.avatar || DEFAULT_AVATAR
}