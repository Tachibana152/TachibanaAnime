<template>
  <div class="post-card card" @click="$emit('open')">
    <div class="head">
      <h3 class="title">
        <el-icon v-if="post.top" color="#e6a23c" class="top-flag"><Top /></el-icon>
        <span>{{ post.title }}</span>
      </h3>
      <el-tag v-if="showStatus" :type="statusType" size="small">{{ statusLabel }}</el-tag>
    </div>
    <p class="content preview">{{ post.content }}</p>
    <div class="foot">
      <span class="author" title="查看主页" @click.stop="$emit('author', post)">
        <el-icon><User /></el-icon>
        {{ post.username }}
      </span>
      <span class="text-muted">{{ post.createTime }}</span>
      <span class="stats">
        <el-icon><View /></el-icon> {{ post.viewCount }}
        <el-icon style="margin-left: 10px"><ChatDotRound /></el-icon> {{ post.replyCount }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { POST_STATUS, POST_STATUS_LABEL } from '@/constants'

const props = defineProps({
  post: { type: Object, required: true },
  showStatus: { type: Boolean, default: false },
})
defineEmits(['open', 'author'])

const statusType = computed(() => {
  if (props.post.status === POST_STATUS.PUBLISHED) return 'success'
  if (props.post.status === POST_STATUS.REJECTED) return 'danger'
  return 'warning'
})
const statusLabel = computed(() => POST_STATUS_LABEL[props.post.status] || '未知')
</script>

<style scoped>
.post-card {
  padding: 16px 18px;
  cursor: pointer;
  transition: box-shadow 0.2s, transform 0.2s;
}
.post-card:hover {
  box-shadow: 0 8px 24px rgba(42, 163, 255, 0.14);
  transform: translateY(-2px);
}
.head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}
.title {
  margin: 0;
  font-size: 17px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 6px;
}
.top-flag {
  flex-shrink: 0;
}
.content {
  margin: 10px 0 14px;
  color: var(--text-light);
  font-size: 14px;
  line-height: 1.7;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.foot {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: var(--text-light);
}
.author {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--primary);
  font-weight: 600;
  cursor: pointer;
}
.stats {
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
</style>