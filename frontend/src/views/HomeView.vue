<template>
  <div class="page-container">
    <div class="hero card">
      <div class="hero-text">
        <h1>Tachibana Anime</h1>
        <p>最新的动画资讯、经典动画推荐与精彩评论。无论你是新番追逐者还是老番爱好者，这里都有丰富的内容。</p>
        <div class="search-box">
          <el-input
            v-model="keyword"
            placeholder="搜索动画：标题 / 原作 / 导演 / 制作 / 简介…"
            size="large"
            clearable
            @keyup.enter="onSearch"
            @clear="onSearch"
          >
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-button type="primary" size="large" @click="onSearch">搜索</el-button>
        </div>
      </div>
    </div>

    <div class="section-header">
      <el-tabs v-model="category" @tab-change="onSearch">
        <el-tab-pane label="全部" name="" />
        <el-tab-pane label="一月新番" name="NEW" />
        <el-tab-pane label="经典动画" name="CLASSIC" />
      </el-tabs>
      <span v-if="keyword" class="result-hint">
        搜索「{{ keyword }}」共 {{ total }} 条结果
        <el-button link type="primary" @click="clearSearch">清除</el-button>
      </span>
    </div>

    <div v-loading="loading">
      <div v-if="list.length" class="anime-grid">
        <AnimeCard
          v-for="a in list"
          :key="a.id"
          :anime="a"
          :keyword="keyword"
          @open="router.push(`/anime/${a.id}`)"
        />
      </div>
      <el-empty v-else :description="keyword ? '未找到相关动画' : '暂无动画数据'" />
    </div>

    <div class="pager">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 36]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @current-change="load"
        @size-change="onSearch"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { animeApi } from '@/api/anime'
import AnimeCard from '@/components/AnimeCard.vue'

const router = useRouter()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(12)
const category = ref('')
const keyword = ref('')
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const data = await animeApi.list({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      category: category.value || undefined,
      keyword: keyword.value || undefined,
    })
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

function onSearch() {
  pageNum.value = 1
  load()
}

function clearSearch() {
  keyword.value = ''
  onSearch()
}

onMounted(load)
</script>

<style scoped>
.hero {
  background: linear-gradient(120deg, #1b7fd4 0%, #2aa3ff 55%, #7c5cff 100%);
  color: #fff;
  padding: 40px 36px;
  margin-bottom: 20px;
}
.hero h1 {
  margin: 0 0 10px;
  font-size: 30px;
}
.hero p {
  margin: 0 0 20px;
  opacity: 0.92;
  font-size: 15px;
}
.search-box {
  display: flex;
  gap: 10px;
  max-width: 640px;
}
.search-box .el-input,
.search-box .el-button {
  border-radius: 22px;
}
.result-hint {
  color: var(--text-light);
  font-size: 13px;
}
.anime-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 18px;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>