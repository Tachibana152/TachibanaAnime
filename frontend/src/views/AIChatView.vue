<template>
  <div class="page-container ai-chat-page">
    <div class="section-header ai-header">
      <h2 class="page-title" style="margin-bottom: 0">AI 助手</h2>
      <div class="session-bar">
        <span class="label">会话ID</span>
        <el-input
          v-model="sessionId"
          placeholder="不同会话ID记忆相互隔离"
          size="default"
          style="width: 220px"
          :disabled="streaming"
        />
        <el-button :icon="Delete" :disabled="!messages.length" @click="clearChat">清空对话</el-button>
      </div>
    </div>

    <div ref="msgBox" v-loading="waiting" class="chat-box">
      <div v-if="!messages.length" class="chat-empty">
        <el-icon :size="40" color="#c0c8d6"><ChatDotRound /></el-icon>
        <p>我是 Tachibana Anime 的 AI 助手，可以为你介绍站内动漫、回答知识库相关问题。</p>
      </div>
      <div v-for="(m, i) in messages" :key="i" class="msg-row" :class="m.role">
        <el-avatar :size="32" class="msg-avatar" :class="m.role">
          {{ m.role === 'user' ? '我' : 'AI' }}
        </el-avatar>
        <div class="bubble" :class="[m.role, m.error ? 'error' : '']">
          {{ m.content }}<span v-if="m.typing" class="cursor">▍</span>
        </div>
      </div>
    </div>

    <div class="input-bar">
      <el-input
        v-model="input"
        type="textarea"
        :rows="2"
        placeholder="输入你的问题，回车发送（Shift+Enter 换行）"
        resize="none"
        :disabled="streaming"
        @keydown.enter.exact.prevent="send"
      />
      <el-button type="primary" :loading="streaming" :disabled="!input.trim()" @click="send">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { aiApi } from '@/api/ai'
import { useUserStore } from '@/stores/user'

const store = useUserStore()
const messages = ref([])
const input = ref('')
const sessionId = ref(`sess-${store.user?.id || 'guest'}`)
const streaming = ref(false)
const waiting = ref(false)
const msgBox = ref(null)

async function scrollToBottom() {
  await nextTick()
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
}

function push(role, content, typing = false) {
  messages.value.push({ role, content, typing })
  scrollToBottom()
}

async function send() {
  const text = input.value.trim()
  if (!text || streaming.value) return
  input.value = ''
  push('user', text)
  push('assistant', '', true)
  streaming.value = true
  const idx = messages.value.length - 1
  try {
    aiApi.chatStream(
      { sessionId: sessionId.value.trim() || 'default', message: text },
      {
        onToken: (t) => {
          messages.value[idx].content += t
          scrollToBottom()
        },
        onDone: () => {
          messages.value[idx].typing = false
          streaming.value = false
          if (!messages.value[idx].content) messages.value[idx].content = '（无回复）'
          scrollToBottom()
        },
        onError: (e) => {
          messages.value[idx].typing = false
          messages.value[idx].error = true
          messages.value[idx].content = e?.message || '请求失败'
          streaming.value = false
          scrollToBottom()
        },
      }
    )
  } catch (e) {
    messages.value[idx].typing = false
    messages.value[idx].error = true
    messages.value[idx].content = e?.message || '请求失败'
    streaming.value = false
  }
}

function clearChat() {
  messages.value = []
}
</script>

<style scoped>
.ai-chat-page {
  max-width: 900px;
  padding-top: 24px;
}
.ai-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
}
.session-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}
.session-bar .label {
  font-size: 13px;
  color: var(--text-light);
}
.chat-box {
  height: calc(100vh - 320px);
  min-height: 320px;
  margin-top: 16px;
  padding: 16px;
  overflow-y: auto;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.chat-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: var(--text-light);
  font-size: 14px;
}
.msg-row {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}
.msg-row.user {
  flex-direction: row-reverse;
}
.msg-avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #2aa3ff, #7c5cff);
  color: #fff;
  font-weight: 600;
  font-size: 13px;
}
.msg-avatar.user {
  background: linear-gradient(135deg, #ff8f3c, #ff5f6d);
}
.bubble {
  max-width: 78%;
  padding: 10px 14px;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-word;
  background: #f4f6fa;
  color: var(--text);
  border: 1px solid var(--border);
  border-top-left-radius: 4px;
}
.bubble.user {
  background: linear-gradient(135deg, #2aa3ff, #1b7fd4);
  color: #fff;
  border: none;
  border-top-right-radius: 4px;
}
.bubble.error {
  background: #fef0f0;
  border: 1px solid #f5c2c2;
  color: #d54949;
  border-top-left-radius: 4px;
}
.cursor {
  animation: blink 1s step-start infinite;
  color: currentColor;
}
@keyframes blink {
  50% {
    opacity: 0;
  }
}
.input-bar {
  display: flex;
  gap: 10px;
  margin-top: 12px;
  align-items: flex-end;
}
.input-bar .el-textarea {
  flex: 1;
}
.input-bar .el-button {
  height: 52px;
}
</style>