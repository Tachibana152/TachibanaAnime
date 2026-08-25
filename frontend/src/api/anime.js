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
  create(form) {
    return USE_MOCK ? mockApi.createAnime(token(), form) : request.post('/animes', form)
  },
  update(id, form) {
    return USE_MOCK ? mockApi.updateAnime(token(), id, form) : request.put(`/animes/${id}`, form)
  },
  remove(id) {
    return USE_MOCK ? mockApi.deleteAnime(token(), id) : request.delete(`/animes/${id}`)
  },
}