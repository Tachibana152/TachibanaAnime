<template>
  <div class="page-container" v-loading="loading">
    <template v-if="anime">
      <div class="anime-bg" :style="{ backgroundImage: `url(${anime.background || anime.cover})` }"></div>
      <div class="crumb">
        <el-button link @click="router.push('/home')"><el-icon><Back /></el-icon> 返回首页</el-button>
      </div>
      <div class="detail-card">
        <p class="page-title">{{ anime.title }}<span v-if="anime.titleJp" class="title-jp"> - {{ anime.titleJp }}</span></p>
        <p class="view-stats text-muted"><el-icon><View /></el-icon> {{ anime.viewCount }} 次浏览</p>
        <div class="twotop"></div>

        <div class="detail-body">
          <!-- 左栏：封面 + 信息列表 -->
          <div class="theleftlist">
            <el-image :src="anime.cover" fit="cover" class="cover">
              <template #error><div class="cover-error">暂无封面</div></template>
            </el-image>
            <ul class="meta-list">
              <li><span class="list">中文名:</span><span class="name" v-html="hl(anime.title)"></span></li>
              <li v-if="anime.titleJp"><span class="list">日文名:</span><span class="name" v-html="hl(anime.titleJp)"></span></li>
              <li v-if="anime.episodes"><span class="list">话数:</span><span class="name">{{ anime.episodes }}</span></li>
              <li v-if="anime.airDate"><span class="list">放送开始:</span><span class="name">{{ anime.airDate }}</span></li>
              <li v-if="anime.airWeekday"><span class="list">放送星期:</span><span class="name">{{ anime.airWeekday }}</span></li>
              <li v-if="anime.original"><span class="list">原作:</span><span class="name" v-html="hl(anime.original)"></span></li>
              <li v-if="anime.director"><span class="list">导演:</span><span class="name" v-html="hl(anime.director)"></span></li>
              <li v-if="anime.writer"><span class="list">脚本:</span><span class="name" v-html="hl(anime.writer)"></span></li>
              <li v-if="anime.storyboard"><span class="list">分镜:</span><span class="name">{{ anime.storyboard }}</span></li>
              <li v-if="anime.performance"><span class="list">演出:</span><span class="name">{{ anime.performance }}</span></li>
              <li v-if="anime.music"><span class="list">音乐:</span><span class="name">{{ anime.music }}</span></li>
              <li v-if="anime.charaOriginal"><span class="list">人物原案:</span><span class="name">{{ anime.charaOriginal }}</span></li>
              <li v-if="anime.charaDesign"><span class="list">人物设定:</span><span class="name">{{ anime.charaDesign }}</span></li>
              <li v-if="anime.seriesComposition"><span class="list">系列构成:</span><span class="name">{{ anime.seriesComposition }}</span></li>
              <li v-if="anime.artDirector"><span class="list">美术监督:</span><span class="name">{{ anime.artDirector }}</span></li>
              <li v-if="anime.colorDesign"><span class="list">色彩设计:</span><span class="name">{{ anime.colorDesign }}</span></li>
              <li v-if="anime.chiefAnimationDirector"><span class="list">总作画监督:</span><span class="name">{{ anime.chiefAnimationDirector }}</span></li>
              <li v-if="anime.animationDirector"><span class="list">作画监督:</span><span class="name">{{ anime.animationDirector }}</span></li>
              <li v-if="anime.photographyDirector"><span class="list">摄影监督:</span><span class="name">{{ anime.photographyDirector }}</span></li>
              <li v-if="anime.planning"><span class="list">企画:</span><span class="name">{{ anime.planning }}</span></li>
              <li v-if="anime.production"><span class="list">动画制作:</span><span class="name" v-html="hl(anime.production)"></span></li>
              <li v-if="anime.alias"><span class="list">别名:</span><span class="name">{{ anime.alias }}</span></li>
            </ul>
          </div>

          <!-- 右栏：简介 / 语录 / 内容 -->
          <div class="themidlist">
            <div class="section-title">简介</div>
            <div class="content1 rich-body" v-html="renderRich(anime.synopsis, route.query.keyword)"></div>

            <div class="quote-box" v-if="anime.quote">
              <span class="qmark qmark-l">"</span>
              <span class="qmark qmark-r">"</span>
              <div class="quote-title">{{ anime.title }}</div>
              <p class="quote-line">{{ anime.quote }}</p>
            </div>

            <div class="section-title">内容</div>
            <div class="content2 rich-body" v-html="renderRich(anime.content, route.query.keyword)"></div>
          </div>
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
import { renderRich } from '@/utils/rich'

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
.anime-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  z-index: -1;
}
.crumb {
  margin-bottom: 12px;
  position: relative;
}
.detail-card {
  max-width: 1200px;
  margin: 0 auto;
  border: 2px solid rgb(221, 221, 221);
  background-color: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(3px);
  border-radius: 10px;
  overflow: hidden;
}
.page-title {
  margin: 0;
  padding: 24px 28px 0;
  font-size: 25px;
  font-weight: 600;
}
.title-jp {
  font-weight: 400;
  color: var(--text-light);
}
.view-stats {
  margin: 6px 28px 0;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
}
.twotop {
  margin-top: 14px;
  border-bottom: 1px solid rgb(173, 173, 173);
  height: 50px;
  background-color: rgba(208, 204, 204, 0.5);
}
.detail-body {
  display: flex;
  gap: 30px;
  padding: 0 28px 28px;
}
.theleftlist {
  width: 280px;
  flex-shrink: 0;
}
.cover {
  width: 280px;
  height: 380px;
  border: 1px solid rgb(0, 0, 0);
  border-radius: 8px;
  display: block;
}
.cover-error {
  width: 280px;
  height: 380px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-light);
  background: #e9e9e9;
  border-radius: 8px;
}
.meta-list {
  list-style: none;
  margin: 20px 0 0;
  padding: 0;
}
.meta-list li {
  border-bottom: 1px solid rgb(192, 187, 187);
  background: rgba(255, 255, 255, 0.45);
  border-radius: 4px;
  padding: 7px;
  font-size: 14px;
  margin-top: 12px;
  line-height: 1.7;
  color: rgb(109, 104, 104);
}
.meta-list .list {
  color: var(--text-light);
  margin-right: 6px;
}
.meta-list .name {
  color: rgb(0, 0, 0);
}
.themidlist {
  flex: 1;
  min-width: 0;
}
.section-title {
  font-size: 25px;
  border-left: 5px solid rgb(4, 137, 19);
  padding-left: 10px;
  margin-top: 26px;
}
.content1 {
  font-size: 15px;
  margin-top: 14px;
  line-height: 26px;
  color: rgb(63, 61, 61);
  padding: 10px;
  background: rgba(255, 255, 255, 0.45);
  border-radius: 4px;
  border-bottom: 1px solid rgb(192, 187, 187);
}
.content2 {
  font-size: 15px;
  margin-top: 14px;
  line-height: 30px;
  color: rgb(63, 61, 61);
  padding: 10px;
  background: rgba(255, 255, 255, 0.45);
  border-radius: 4px;
  border-bottom: 1px solid rgb(192, 187, 187);
}
.quote-box {
  position: relative;
  margin: 34px 0 10px;
  padding: 6px 40px 16px 60px;
  font-style: italic;
  background: rgba(255, 255, 255, 0.35);
  border-radius: 6px;
}
.qmark {
  position: absolute;
  font-size: 80px;
  line-height: 1;
  color: rgba(7, 19, 245, 0.3);
  font-family: Georgia, 'Times New Roman', serif;
}
.qmark-l {
  top: -6px;
  left: 8px;
}
.qmark-r {
  top: -6px;
  right: 8px;
}
.quote-title {
  font-size: 22px;
  color: rgba(0, 0, 0, 0.8);
}
.quote-line {
  margin: 12px 0 0;
  font-size: 18px;
  color: rgba(0, 0, 0, 0.8);
}
</style>