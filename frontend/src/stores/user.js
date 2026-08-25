import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'
import { ROLE, TOKEN_KEY, USER_KEY } from '@/constants'

function readUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch {
    return null
  }
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || '',
    user: readUser(),
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
    role: (s) => s.user?.role || '',
    isAdmin: (s) => s.user?.role === ROLE.ADMIN || s.user?.role === ROLE.SUPER_ADMIN,
    isSuperAdmin: (s) => s.user?.role === ROLE.SUPER_ADMIN,
  },
  actions: {
    setAuth(token, user) {
      this.token = token
      this.user = user
      localStorage.setItem(TOKEN_KEY, token)
      localStorage.setItem(USER_KEY, JSON.stringify(user))
    },
    async login(form) {
      const data = await authApi.login(form)
      this.setAuth(data.token, data.user)
      return data
    },
    async register(form) {
      return authApi.register(form)
    },
    async fetchMe() {
      const user = await authApi.me()
      this.user = user
      localStorage.setItem(USER_KEY, JSON.stringify(user))
      return user
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
    },
  },
})