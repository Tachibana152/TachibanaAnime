<template>
  <div class="page-container">
    <div class="section-header">
      <h2 class="page-title" style="margin-bottom: 0">我的帖子</h2>
      <el-button type="primary" round @click="router.push('/post/edit')">
        <el-icon><EditPen /></el-icon> 发新帖
      </el-button>
    </div>

    <div v-loading="loading">
      <div v-if="list.length" class="mine-list">
        <div v-for="p in list" :key="p.id" class="mine-item card">
          <div class="head">
            <span class="title" @click="openPost(p)">{{ p.title }}</span>
            <el-tag :type="statusType(p.status)" size="small">{{ statusLabel(p.status) }}</el-tag>
          </div>
          <p class="text-muted">{{ p.createTime }} · 浏览 {{ p.viewCount }} · 回复 {{ p.replyCount }}</p>
          <p v-if="p.status === 2 && p.rejectReason" class="reject">
            驳回原因：{{ p.rejectReason }}
          </p>
          <div class="ops">
            <el-button v-if="p.status !== 1" link type="primary" @click="router.push(`/post/edit/${p.id}`)">编辑重提</el-button>
            <el-button v-else link type="primary" @click="router.push(`/post/${p.id}`)">查看</el-button>
            <el-button link type="danger" @click="onDelete(p)">删除</el-button>
          </div>
        </div>
      </div>
      <el-empty v-else description="你还没有发过帖子" />
    </div>

    <div class="pager">
      <el-pagination
        v-model:current-page="pageNum"
        :page-size="pageSize"
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { forumApi } from '@/api/forum'
import { POST_STATUS, POST_STATUS_LABEL } from '@/constants'

const router = useRouter()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const statusType = (s) => (s === POST_STATUS.PUBLISHED ? 'success' : s === POST_STATUS.REJECTED ? 'danger' : 'warning')
const statusLabel = (s) => POST_STATUS_LABEL[s]

function openPost(p) {
  if (p.status === POST_STATUS.PUBLISHED) router.push(`/post/${p.id}`)
  else ElMessage.info('帖子尚未发布，可编辑后重新提交审核')
}

async function load() {
  loading.value = true
  try {
    const data = await forumApi.myPosts({ pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

async function onDelete(p) {
  await ElMessageBox.confirm(`确定删除帖子「${p.title}」吗？`, '提示', { type: 'warning' })
  try {
    await forumApi.deletePost(p.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(load)
</script>

<style scoped>
.mine-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.mine-item {
  padding: 16px 20px;
}
.head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}
.title {
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
}
.reject {
  margin: 8px 0 0;
  color: #e5484d;
  background: rgba(229, 72, 77, 0.08);
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 13px;
}
.ops {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>