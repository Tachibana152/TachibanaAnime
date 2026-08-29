import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

export const userApi = {
  profile(id) {
    return USE_MOCK ? mockApi.userProfile(id) : request.get(`/users/${id}`)
  },
  userPosts(id, params) {
    return USE_MOCK ? mockApi.userPosts(id, params) : request.get(`/users/${id}/posts`, { params })
  },
  userAnimes(id, params) {
    return USE_MOCK ? mockApi.userAnimes(id, params) : request.get(`/users/${id}/animes`, { params })
  },
  listAdmins() {
    return USE_MOCK ? mockApi.listAdmins(token()) : request.get('/users/admins')
  },
  listUsers(params) {
    return USE_MOCK ? mockApi.listUsers(token(), params) : request.get('/admin/users', { params })
  },
  updateStatus(id, status) {
    return USE_MOCK ? mockApi.updateUserStatus(token(), id, status) : request.put(`/admin/users/${id}/status`, { status })
  },
  updateRole(id, role) {
    return USE_MOCK ? mockApi.updateUserRole(token(), id, role) : request.put(`/admin/users/${id}/role`, { role })
  },
  removeUser(id) {
    return USE_MOCK ? mockApi.deleteUser(token(), id) : request.delete(`/admin/users/${id}`)
  },

  // 头像审核（超级管理员）
  listAvatarAudits() {
    return USE_MOCK ? mockApi.listAvatarAudits(token()) : request.get('/admin/users/avatar-audits')
  },
  reviewAvatar(id, approve) {
    return USE_MOCK ? mockApi.reviewAvatar(token(), id, approve) : request.put(`/admin/users/avatar-audits/${id}`, { approve })
  },
}

export const fileApi = {
  upload(file, type) {
    if (USE_MOCK) {
      return mockApi.upload(token(), file, type)
    }
    const formData = new FormData()
    formData.append('file', file)
    if (type) formData.append('type', type)
    return request.post('/files/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
}