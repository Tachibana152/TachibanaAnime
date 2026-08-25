import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

// 是否使用内置 Mock（后端未就绪前置 true）
export const USE_MOCK = import.meta.env.VITE_USE_MOCK !== 'false'

export const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

request.interceptors.request.use((config) => {
  const store = useUserStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

function handleUnauthorized() {
  const store = useUserStore()
  store.logout()
  if (window.location.pathname !== '/login') {
    ElMessage.error('登录已过期，请重新登录')
    window.location.href = '/login'
  }
}

request.interceptors.response.use(
  (response) => {
    const r = response.data
    if (r && typeof r.code !== 'undefined') {
      if (r.code === 200) return r.data
      if (r.code === 401) {
        handleUnauthorized()
      }
      return Promise.reject(new Error(r.message || '请求失败'))
    }
    return r
  },
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      handleUnauthorized()
    } else if (status === 403) {
      ElMessage.error('没有权限执行该操作')
    } else if (status >= 500) {
      ElMessage.error('服务器内部错误')
    }
    const msg = error.response?.data?.message || error.message || '网络异常'
    return Promise.reject(new Error(msg))
  }
)

// 统一错误提示辅助：页面 catch 里调用
export function toastError(err) {
  ElMessage.error(err?.message || '操作失败')
}