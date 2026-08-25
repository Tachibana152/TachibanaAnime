<template>
  <div class="page-container" v-loading="loading">
    <template v-if="anime">
      <div class="crumb">
        <el-button link @click="router.push('/home')"><el-icon><Back /></el-icon> 返回首页</el-button>
      </div>
      <div class="detail card">
        <div class="left">
          <div class="poster-wrap">
            <el-image :src="anime.cover" fit="cover" class="poster">
              <template #error><div class="poster-error">暂无封面</div></template>
            </el-image>
            <el-tag :type="anime.category === 'NEW' ? 'danger' : 'warning'" size="small" effect="dark" class="cat">
              {{ anime.category === 'NEW' ? '一月新番' : '经典动画' }}
            </el-tag>
          </div>
          <ul class="meta-list">
            <li><span class="k">中文名</span><span class="v" v-html="hl(anime.title)"></span></li>
            <li v-if="anime.titleJp"><span class="k">日文名</span><span class="v" v-html="hl(anime.titleJp)"></span></li>
            <li><span class="k">话数</span><span class="v">{{ anime.episodes }}</span></li>
            <li><span class="k">放送开始</span><span class="v">{{ anime.airDate }}</span></li>
            <li v-if="anime.airWeekday"><span class="k">放送星期</span><span class="v">{{ anime.airWeekday }}</span></li>
            <li v-if="anime.original"><span class="k">原作</span><span class="v" v-html="hl(anime.original)"></span></li>
            <li v-if="anime.director"><span class="k">导演</span><span class="v" v-html="hl(anime.director)"></span></li>
            <li v-if="anime.writer"><span class="k">脚本</span><span class="v" v-html="hl(anime.writer)"></span></li>
            <li v-if="anime.production"><span class="k">制作</span><span class="v" v-html="hl(anime.production)"></span></li>
          </ul>
        </div>
        <div class="right">
          <h1 class="title" v-html="hl(anime.title)"></h1>
          <p class="sub" v-if="anime.titleJp" v-html="hl(anime.titleJp)"></p>
          <div class="stats text-muted">
            <el-icon><View /></el-icon> {{ anime.viewCount }} 次浏览
          </div>

          <h2 class="section">简介</h2>
          <p class="rich-text synopsis" v-html="hl(anime.synopsis)"></p>

          <h2 class="section">内容</h2>
          <div class="rich-text content" v-html="hl(anime.content)"></div>
        </div>
      </div>
    </template>
    <el-empty v-else-if="!loading" description="动画不存在或已下架" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { animeApi } from '@/api/anime'
import { highlightText } from '@/utils/highlight'

const route = useRoute()
const router = useRouter()
const anime = ref(null)
const loading = ref(false)

function hl(text) {
  return highlightText(text || '', route.query.keyword || '')
}

async function load() {
  loading.value = true
  try {
    anime.value = await animeApi.detail(route.params.id)
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

watch(() => route.params.id, load)
onMounted(load)
</script>

<style scoped>
.crumb {
  margin-bottom: 12px;
}
.detail {
  display: flex;
  gap: 28px;
  padding: 28px;
}
.left {
  width: 280px;
  flex-shrink: 0;
}
.poster-wrap {
  position: relative;
}
.poster {
  width: 280px;
  height: 380px;
  border-radius: 10px;
  box-shadow: var(--shadow);
}
.poster-error {
  width: 280px;
  height: 380px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-light);
  background: #f0f2f5;
}
.cat {
  position: absolute;
  top: 10px;
  left: 10px;
}
.meta-list {
  list-style: none;
  margin: 14px 0 0;
  padding: 0;
}
.meta-list li {
  display: flex;
  padding: 7px 0;
  border-bottom: 1px dashed var(--border);
  font-size: 14px;
  line-height: 1.6;
}
.k {
  width: 76px;
  flex-shrink: 0;
  color: var(--text-light);
}
.v {
  flex: 1;
  color: var(--text);
}
.right {
  flex: 1;
  min-width: 0;
}
.title {
  margin: 0 0 4px;
  font-size: 26px;
  font-weight: 700;
}
.sub {
  margin: 0 0 8px;
  color: var(--text-light);
  font-size: 15px;
}
.stats {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.section {
  font-size: 18px;
  border-left: 5px solid var(--primary);
  padding-left: 10px;
  margin: 22px 0 12px;
}
.synopsis {
  background: #f8faff;
  padding: 16px 18px;
  border-radius: 10px;
}
.content {
  color: #555;
}
</style>