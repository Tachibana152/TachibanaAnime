<template>
  <div class="reply-item">
    <el-avatar :size="34" class="r-avatar">{{ reply.username?.[0] || 'U' }}</el-avatar>
    <div class="r-body">
      <div class="r-head">
        <span class="r-name" title="查看主页" @click="$emit('author')">{{ reply.username }}</span>
        <span class="text-muted">{{ reply.createTime }}</span>
        <span v-if="canDelete" class="r-del" @click="$emit('remove')">删除</span>
      </div>
      <p class="r-content rich-text">{{ reply.content }}</p>
      <div class="r-actions">
        <span class="like-btn" :class="{ liked: reply.liked }" title="点赞" @click="$emit('like')">
          <el-icon><Pointer /></el-icon>
          <span>{{ reply.likeCount || 0 }}</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  reply: { type: Object, required: true },
})
defineEmits(['remove', 'author', 'like'])

const store = useUserStore()
const canDelete = computed(() => {
  if (!store.isLoggedIn) return false
  return store.user?.id === props.reply.userId || store.isAdmin
})
</script>

<style scoped>
.reply-item {
  display: flex;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid var(--border);
}
.r-avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #2aa3ff, #7c5cff);
  color: #fff;
  font-weight: 600;
}
.r-body {
  flex: 1;
  min-width: 0;
}
.r-head {
  display: flex;
  align-items: center;
  gap: 12px;
}
.r-name {
  font-weight: 600;
  color: var(--primary);
  font-size: 14px;
  cursor: pointer;
}
.r-del {
  margin-left: auto;
  font-size: 12px;
  color: var(--text-light);
  cursor: pointer;
}
.r-del:hover {
  color: #e5484d;
}
.r-content {
  margin: 6px 0 0;
}
.r-actions {
  margin-top: 6px;
}
.like-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-light);
  cursor: pointer;
  padding: 2px 8px;
  border-radius: 12px;
  transition: all 0.2s;
  user-select: none;
}
.like-btn:hover {
  color: var(--primary);
  background: rgba(42, 163, 255, 0.1);
}
.like-btn.liked {
  color: var(--primary);
  font-weight: 600;
}
.like-btn.liked :deep(.el-icon) {
  color: var(--primary);
}
</style>