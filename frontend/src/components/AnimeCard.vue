<template>
  <div class="anime-card card" @click="$emit('open')">
    <div class="cover-wrap">
      <el-image :src="anime.cover" fit="cover" class="cover" lazy>
        <template #error>
          <div class="cover-error"><el-icon><Picture /></el-icon></div>
        </template>
      </el-image>
      <el-tag :type="anime.category === 'NEW' ? 'danger' : 'warning'" size="small" class="cat-tag" effect="dark">
        {{ anime.category === 'NEW' ? '新番' : '经典' }}
      </el-tag>
    </div>
    <div class="info">
      <h3 class="title" :title="anime.title">
        <span v-html="highlight(anime.title)"></span>
      </h3>
      <p class="meta"><span v-html="highlight(anime.original || '')"></span></p>
      <p class="meta"><span v-html="highlight(anime.production || '')"></span></p>
      <div class="bottom">
        <span class="text-muted">{{ anime.episodes }}话</span>
        <span class="text-muted">{{ anime.airDate }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { highlightText } from '@/utils/highlight'

const props = defineProps({
  anime: { type: Object, required: true },
  keyword: { type: String, default: '' },
})
defineEmits(['open'])

function highlight(text) {
  return highlightText(text || '', props.keyword)
}
</script>

<style scoped>
.anime-card {
  overflow: hidden;
  background: #fff;
}
.cover-wrap {
  position: relative;
  height: 260px;
  overflow: hidden;
}
.cover {
  width: 100%;
  height: 100%;
}
.cover-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-light);
  font-size: 32px;
  background: #f0f2f5;
}
.cat-tag {
  position: absolute;
  top: 8px;
  left: 8px;
}
.info {
  padding: 12px 14px 14px;
}
.title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 700;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.meta {
  margin: 0 0 4px;
  font-size: 13px;
  color: var(--text-light);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.bottom {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}
</style>