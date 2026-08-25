import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

export const userApi = {
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
}

export const fileApi = {
  upload(file) {
    if (USE_MOCK) {
      return mockApi.upload(token(), file)
    }
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/files/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
}