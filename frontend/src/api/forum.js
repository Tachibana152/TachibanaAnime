import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

export const forumApi = {
  listPosts(params) {
    return USE_MOCK ? mockApi.listPosts(params) : request.get('/forum/posts', { params })
  },
  postDetail(id) {
    return USE_MOCK ? mockApi.postDetail(id) : request.get(`/forum/posts/${id}`)
  },
  myPosts(params) {
    return USE_MOCK ? mockApi.myPosts(token(), params) : request.get('/forum/posts/mine', { params })
  },
  createPost(form) {
    return USE_MOCK ? mockApi.createPost(token(), form) : request.post('/forum/posts', form)
  },
  updatePost(id, form) {
    return USE_MOCK ? mockApi.updatePost(token(), id, form) : request.put(`/forum/posts/${id}`, form)
  },
  deletePost(id) {
    return USE_MOCK ? mockApi.deletePost(token(), id) : request.delete(`/forum/posts/${id}`)
  },
  toggleTop(id, top) {
    return USE_MOCK ? mockApi.toggleTop(token(), id, top) : request.put(`/forum/posts/${id}/top`, { top })
  },

  // 审核
  listAdminPosts(params) {
    return USE_MOCK ? mockApi.listAdminPosts(token(), params) : request.get('/admin/posts', { params })
  },
  adminPostDetail(id) {
    return USE_MOCK ? mockApi.adminPostDetail(token(), id) : request.get(`/admin/posts/${id}`)
  },
  reviewPost(id, form) {
    return USE_MOCK ? mockApi.reviewPost(token(), id, form) : request.put(`/admin/posts/${id}/review`, form)
  },

  // 回复
  listReplies(postId, params) {
    return USE_MOCK ? mockApi.listReplies(postId, params) : request.get(`/forum/posts/${postId}/replies`, { params })
  },
  createReply(postId, form) {
    return USE_MOCK ? mockApi.createReply(token(), postId, form) : request.post(`/forum/posts/${postId}/replies`, form)
  },
  deleteReply(id) {
    return USE_MOCK ? mockApi.deleteReply(token(), id) : request.delete(`/forum/posts/replies/${id}`)
  },
}