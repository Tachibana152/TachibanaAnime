<template>
  <div class="page-container">
    <h2 class="page-title">帖子管理 / 审核</h2>

    <el-tabs v-model="activeTab" @tab-change="onSearch">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待审核" name="0" />
      <el-tab-pane label="已发布" name="1" />
      <el-tab-pane label="已驳回" name="2" />
    </el-tabs>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索标题…" clearable style="width: 260px" @keyup.enter="onSearch" @clear="onSearch" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-badge v-if="activeTab !== '0'" :value="pendingCount" :max="99" class="pending-badge">
        <span class="text-muted">待审核帖数</span>
      </el-badge>
    </div>

    <el-table v-loading="loading" :data="list" border stripe class="mt-16">
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="username" label="作者" width="110" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="replyCount" label="回复" width="70" />
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column prop="createTime" label="发布时间" width="160" />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="preview(row)">查看</el-button>
          <el-button v-if="row.status !== 0" link @click="toggleTop(row)">
            {{ row.top ? '取消置顶' : '置顶' }}
          </el-button>
          <el-button v-if="row.status === 0" link type="success" @click="openReview(row)">审核</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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

    <!-- 审核弹窗 -->
    <el-dialog v-model="reviewVisible" title="帖子审核" width="640px">
      <div class="review-content">
        <h3>{{ current?.title }}</h3>
        <p class="text-muted">作者：{{ current?.username }} · {{ current?.createTime }}</p>
        <div class="rich-body review-body" v-html="renderRich(current?.content)"></div>
      </div>
      <el-form label-position="top">
        <el-form-item label="审核结果">
          <el-radio-group v-model="reviewForm.status">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="reviewForm.status === 2" label="驳回原因" required>
          <el-input v-model="reviewForm.rejectReason" placeholder="请填写驳回原因，作者可见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" :loading="reviewing" @click="submitReview">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { forumApi } from '@/api/forum'
import { POST_STATUS, POST_STATUS_LABEL } from '@/constants'
import { renderRich } from '@/utils/rich'

const activeTab = ref('all')
const keyword = ref('')
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const pendingCount = ref(0)

const reviewVisible = ref(false)
const reviewing = ref(false)
const current = ref(null)
const reviewForm = reactive({ status: 1, rejectReason: '' })

const statusType = (s) => (s === POST_STATUS.PUBLISHED ? 'success' : s === POST_STATUS.REJECTED ? 'danger' : 'warning')
const statusLabel = (s) => POST_STATUS_LABEL[s]

async function loadPendingCount() {
  try {
    const data = await forumApi.listAdminPosts({ status: POST_STATUS.PENDING, pageNum: 1, pageSize: 1 })
    pendingCount.value = data.total || 0
  } catch { /* ignore */ }
}

async function load() {
  loading.value = true
  try {
    const data = await forumApi.listAdminPosts({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: activeTab.value === 'all' ? undefined : Number(activeTab.value),
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
  loadPendingCount()
}

async function preview(row) {
  try {
    const detail = await forumApi.adminPostDetail(row.id)
    await ElMessageBox.alert(detail.content, `帖子详情：${detail.title}`, {
      confirmButtonText: '关闭',
      dangerouslyUseHTMLString: false,
      customClass: 'preview-dialog',
    })
  } catch (e) {
    ElMessage.error(e.message)
  }
}

function openReview(row) {
  current.value = row
  reviewForm.status = 1
  reviewForm.rejectReason = ''
  reviewVisible.value = true
}

async function submitReview() {
  if (reviewForm.status === 2 && !reviewForm.rejectReason.trim()) {
    return ElMessage.warning('请填写驳回原因')
  }
  reviewing.value = true
  try {
    await forumApi.reviewPost(current.value.id, {
      status: reviewForm.status,
      rejectReason: reviewForm.rejectReason.trim(),
    })
    ElMessage.success(reviewForm.status === 1 ? '已通过，帖子已发布' : '已驳回')
    reviewVisible.value = false
    load()
    loadPendingCount()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    reviewing.value = false
  }
}

async function toggleTop(row) {
  try {
    await forumApi.toggleTop(row.id, row.top ? 0 : 1)
    ElMessage.success(row.top ? '已取消置顶' : '已置顶')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '警告', { type: 'warning' })
  try {
    await forumApi.deletePost(row.id)
    ElMessage.success('已删除')
    load()
    loadPendingCount()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(() => {
  load()
  loadPendingCount()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
}
.pending-badge {
  margin-left: auto;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
.review-content {
  background: #f8faff;
  border-radius: 8px;
  padding: 14px 18px;
  margin-bottom: 12px;
}
.review-content h3 {
  margin: 0 0 6px;
}
.review-body {
  max-height: 200px;
  overflow: auto;
  font-size: 14px;
}
</style>