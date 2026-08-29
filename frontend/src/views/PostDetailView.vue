<template>
  <div class="page-container" v-loading="loading">
    <template v-if="post">
      <div class="crumb">
        <el-button link @click="router.push('/forum')"><el-icon><Back /></el-icon> 返回论坛</el-button>
      </div>

      <div class="post card">
        <h1 class="title">
          <el-icon v-if="post.top" color="#e6a23c"><Top /></el-icon>
          {{ post.title }}
        </h1>
        <div class="meta">
          <span class="author">{{ post.username }}</span>
          <span class="text-muted">{{ post.createTime }}</span>
          <span class="text-muted"><el-icon><View /></el-icon> {{ post.viewCount }}</span>
          <el-dropdown v-if="canManage" trigger="click" class="ops">
            <el-button link type="primary">操作</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="canDelete" @click="onDeletePost">删除帖子</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <div class="rich-body body" v-html="renderRich(post.content)"></div>

        <a
          v-if="post.sourceUrl"
          :href="post.sourceUrl"
          target="_blank"
          rel="noopener"
          class="source"
        >点击查看更多 →</a>
      </div>

      <div class="reply-section card">
        <h3 class="reply-title">回复（{{ post.replyCount }}）</h3>

        <div v-if="store.isLoggedIn" class="reply-box">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="3"
            maxlength="500"
            show-word-limit
            placeholder="说点什么吧…"
          />
          <div class="reply-actions">
            <el-button type="primary" :loading="replying" @click="submitReply">发表回复</el-button>
          </div>
        </div>
        <el-empty v-else description="登录后参与讨论" :image-size="80">
          <el-button type="primary" round @click="router.push(`/login?redirect=${route.fullPath}`)">去登录</el-button>
        </el-empty>

        <div v-loading="replyLoading" class="reply-list">
          <ReplyItem
            v-for="r in replies"
            :key="r.id"
            :reply="r"
            @remove="onDeleteReply(r)"
          />
          <el-empty v-if="!replyLoading && !replies.length" description="还没有回复，快来抢沙发" :image-size="70" />
        </div>

        <div class="pager">
          <el-pagination
            v-model:current-page="replyPage"
            :page-size="replyPageSize"
            :total="replyTotal"
            layout="total, prev, pager, next"
            background
            small
            @current-change="loadReplies"
          />
        </div>
      </div>
    </template>
    <el-empty v-else-if="!loading" description="帖子不存在或未发布" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { forumApi } from '@/api/forum'
import { useUserStore } from '@/stores/user'
import { renderRich } from '@/utils/rich'
import ReplyItem from '@/components/ReplyItem.vue'

const route = useRoute()
const router = useRouter()
const store = useUserStore()

const post = ref(null)
const loading = ref(false)
const replies = ref([])
const replyLoading = ref(false)
const replyPage = ref(1)
const replyPageSize = ref(10)
const replyTotal = ref(0)
const replyContent = ref('')
const replying = ref(false)

const canManage = computed(() => store.isLoggedIn && (store.isAdmin || store.user?.id === post.value?.userId))
const canDelete = computed(() => store.isLoggedIn && (store.isAdmin || store.user?.id === post.value?.userId))

async function loadPost() {
  loading.value = true
  try {
    post.value = await forumApi.postDetail(route.params.id)
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

async function loadReplies() {
  replyLoading.value = true
  try {
    const data = await forumApi.listReplies(route.params.id, { pageNum: replyPage.value, pageSize: replyPageSize.value })
    replies.value = data.records || []
    replyTotal.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    replyLoading.value = false
  }
}

async function submitReply() {
  if (!replyContent.value.trim()) return ElMessage.warning('请输入回复内容')
  replying.value = true
  try {
    await forumApi.createReply(route.params.id, { content: replyContent.value.trim() })
    replyContent.value = ''
    ElMessage.success('回复成功')
    if (post.value) post.value.replyCount += 1
    replyPage.value = 1
    loadReplies()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    replying.value = false
  }
}

async function onDeleteReply(r) {
  await ElMessageBox.confirm('确定删除这条回复吗？', '提示', { type: 'warning' })
  try {
    await forumApi.deleteReply(r.id)
    ElMessage.success('已删除')
    if (post.value) post.value.replyCount = Math.max(0, post.value.replyCount - 1)
    loadReplies()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function onDeletePost() {
  await ElMessageBox.confirm('确定删除这篇帖子吗？删除后不可恢复。', '警告', { type: 'warning' })
  try {
    await forumApi.deletePost(post.value.id)
    ElMessage.success('已删除')
    router.push('/forum')
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(() => {
  loadPost()
  loadReplies()
})
</script>

<style scoped>
.crumb {
  margin-bottom: 12px;
}
.post {
  padding: 28px 32px;
}
.title {
  margin: 0 0 10px;
  font-size: 24px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 8px;
}
.meta {
  display: flex;
  align-items: center;
  gap: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
  font-size: 13px;
}
.author {
  color: var(--primary);
  font-weight: 600;
  font-size: 14px;
}
.meta .el-icon {
  vertical-align: -2px;
}
.ops {
  margin-left: auto;
}
.body {
  margin: 22px 0;
}
.source {
  color: #ff18fb;
  font-size: 14px;
  font-weight: 600;
}
.reply-section {
  margin-top: 20px;
  padding: 22px 28px;
}
.reply-title {
  margin: 0 0 16px;
  font-size: 17px;
}
.reply-box {
  margin-bottom: 16px;
}
.reply-actions {
  margin-top: 10px;
  text-align: right;
}
.reply-list {
  min-height: 60px;
}
.pager {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}
</style>