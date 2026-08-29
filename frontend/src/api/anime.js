import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

export const animeApi = {
  list(params) {
    return USE_MOCK ? mockApi.listAnimes(params) : request.get('/animes', { params })
  },
  detail(id) {
    return USE_MOCK ? mockApi.animeDetail(id) : request.get(`/animes/${id}`)
  },
  contributors(id) {
    return USE_MOCK ? mockApi.animeContributors(id) : request.get(`/animes/${id}/contributors`)
  },
  create(form) {
    return USE_MOCK ? mockApi.createAnime(token(), form) : request.post('/animes', form)
  },
  update(id, form) {
    return USE_MOCK ? mockApi.updateAnime(token(), id, form) : request.put(`/animes/${id}`, form)
  },
  remove(id) {
    return USE_MOCK ? mockApi.deleteAnime(token(), id) : request.delete(`/animes/${id}`)
  },

  // 动漫评论
  listComments(id, params) {
    return USE_MOCK ? mockApi.listAnimeComments(id, params) : request.get(`/animes/${id}/comments`, { params })
  },
  createComment(id, form) {
    return USE_MOCK ? mockApi.createAnimeComment(token(), id, form) : request.post(`/animes/${id}/comments`, form)
  },
  deleteComment(id) {
    return USE_MOCK ? mockApi.deleteAnimeComment(token(), id) : request.delete(`/animes/comments/${id}`)
  },
  toggleCommentLike(id) {
    return USE_MOCK ? mockApi.toggleAnimeCommentLike(token(), id) : request.post(`/animes/comments/${id}/like`)
  },
}