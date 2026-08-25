import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

export const authApi = {
  login(form) {
    return USE_MOCK ? mockApi.login(form) : request.post('/auth/login', form)
  },
  register(form) {
    return USE_MOCK ? mockApi.register(form) : request.post('/auth/register', form)
  },
  me() {
    return USE_MOCK ? mockApi.me(token()) : request.get('/auth/me')
  },
}