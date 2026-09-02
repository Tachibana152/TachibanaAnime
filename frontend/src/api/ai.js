import { request, USE_MOCK } from './client'
import { mockApi } from '@/mock'
import { useUserStore } from '@/stores/user'

function token() {
  return useUserStore().token
}

// 用 fetch 发流式请求：POST + Authorization，按 SSE data: 帧拆流，逐块回调
// 后端异常会以 "[error] 原因" 帧下发（SSE 错误对 fetch 不可见），此处识别后转 onError
async function fetchStream(payload, { onToken, onDone, onError }) {
  const controller = new AbortController()
  const timer = setTimeout(() => controller.abort(), 90000)
  let reportedError = false
  const fail = (msg) => {
    if (reportedError) return
    reportedError = true
    onError?.(new Error(msg))
  }
  try {
    const resp = await fetch('/api/ai/chat/stream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token()}`,
      },
      body: JSON.stringify(payload),
      signal: controller.signal,
    })
    if (!resp.ok || !resp.body) {
      const text = await resp.text().catch(() => '')
      throw new Error(`请求失败(${resp.status}) ${text}`)
    }
    const reader = resp.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    for (;;) {
      const { done, value } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })
      // 按 SSE 事件分隔（\n\n）拆分，兼容无 data: 前缀的裸 chunk
      let idx
      while ((idx = buffer.indexOf('\n\n')) >= 0) {
        const raw = buffer.slice(0, idx)
        buffer = buffer.slice(idx + 2)
        const lines = raw.split('\n')
        const dataLines = lines.filter((l) => l.startsWith('data:'))
        const text = dataLines.length
          ? dataLines.map((l) => l.slice(5).trimStart()).join('\n')
          : raw.trim()
        if (!text) continue
        if (text.startsWith('[error]')) {
          fail(text.slice(7).trim())
          return
        }
        onToken?.(text)
      }
    }
    if (!reportedError) onDone?.()
  } catch (e) {
    const msg = e?.name === 'AbortError' ? '请求超时，请稍后重试' : (e?.message || '请求失败')
    fail(msg)
  } finally {
    clearTimeout(timer)
  }
}

// Mock 模式下把模拟回复拆成片段逐个回调（无后端也能演示打字机效果）
function mockStream(reply, { onToken, onDone, onError }) {
  try {
    const chars = reply.split('')
    let i = 0
    const timer = setInterval(() => {
      if (i >= chars.length) {
        clearInterval(timer)
        onDone?.()
        return
      }
      onToken?.(chars[i])
      i++
    }, 30)
    return () => clearInterval(timer)
  } catch (e) {
    onError?.(e)
  }
}

export const aiApi = {
  chat(payload) {
    return USE_MOCK ? mockApi.chat(token(), payload) : request.post('/ai/chat', payload)
  },
  chatStream(payload, { onToken, onDone, onError } = {}) {
    if (USE_MOCK) {
      return mockApi.chat(token(), payload).then((reply) => mockStream(reply, { onToken, onDone, onError }))
    }
    return fetchStream(payload, { onToken, onDone, onError })
  },
}