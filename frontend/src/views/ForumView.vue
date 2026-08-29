<template>
  <div class="page-container">
    <div class="section-header">
      <h2 class="page-title" style="margin-bottom: 0">动画论坛</h2>
      <el-button v-if="store.isLoggedIn" type="primary" round @click="router.push('/post/edit')">
        <el-icon><EditPen /></el-icon> 发帖
      </el-button>
      <el-button v-else type="primary" round @click="router.push('/login')">登录后发帖</el-button>
    </div>

    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索帖子标题 / 内容…" clearable @keyup.enter="onSearch" @clear="onSearch">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="onSearch">搜索</el-button>
    </div>

    <div v-loading="loading" class="mt-16">
      <div v-if="list.length" class="post-list">
        <PostCard v-for="p in list" :key="p.id" :post="p" @open="router.push(`/post/${p.id}`)" @author="(p) => router.push(`/user/${p.userId}`)" />
      </div>
      <el-empty v-else :description="keyword ? '未找到相关帖子' : '暂无帖子'" />
    </div>

    <div class="pager">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        background
        @current-change="load"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { forumApi } from '@/api/forum'
import { useUserStore } from '@/stores/user'
import PostCard from '@/components/PostCard.vue'

const router = useRouter()
const store = useUserStore()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const keyword = ref('')
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const data = await forumApi.listPosts({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
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

onMounted(load)
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 10px;
  max-width: 560px;
}
.post-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>