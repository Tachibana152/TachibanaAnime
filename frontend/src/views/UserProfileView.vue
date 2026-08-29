<template>
  <div class="page-container" v-loading="loading">
    <template v-if="profile">
      <!-- 头部信息卡 -->
      <div class="profile-card card">
        <el-avatar :size="88" :src="avatarUrl(profile)" class="avatar">
          <template #error><span class="avatar-fallback">{{ (profile.nickname || profile.username)?.[0] || 'U' }}</span></template>
        </el-avatar>
        <div class="info">
          <div class="name-row">
            <h2 class="name">{{ profile.nickname || profile.username }}</h2>
            <el-tag :type="roleTagType" size="small">{{ roleLabel }}</el-tag>
            <el-button v-if="isSelf" type="primary" round size="small" style="margin-left: 6px" @click="router.push('/settings')">
              <el-icon><EditPen /></el-icon> 编辑资料
            </el-button>
          </div>
          <p class="username">@{{ profile.username }}</p>
          <p v-if="profile.bio" class="bio">{{ profile.bio }}</p>
          <p class="text-muted join">加入于 {{ profile.createTime }}</p>
        </div>
        <div class="stats">
          <div class="stat"><b>{{ profile.postCount }}</b><span>帖子</span></div>
          <div class="stat"><b>{{ profile.animeCount }}</b><span>动漫贡献</span></div>
        </div>
      </div>

      <!-- Tab 内容 -->
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane :label="`帖子（${profile.postCount}）`" name="posts">
          <div v-loading="tabLoading" class="tab-body">
            <div v-if="posts.length" class="post-list">
              <PostCard v-for="p in posts" :key="p.id" :post="p" @open="router.push(`/post/${p.id}`)" @author="() => router.push(`/user/${route.params.id}`)" />
            </div>
            <el-empty v-else-if="!tabLoading" description="TA 还没有发布帖子" :image-size="80" />
            <div v-if="postsTotal > pageSize" class="pager">
              <el-pagination
                v-model:current-page="pageNum"
                :page-size="pageSize"
                :total="postsTotal"
                layout="total, prev, pager, next"
                background
                small
                @current-change="loadPosts"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane :label="`动漫条目（${profile.animeCount}）`" name="animes">
          <div v-loading="tabLoading" class="tab-body">
            <div v-if="animes.length" class="anime-grid">
              <AnimeCard v-for="a in animes" :key="a.id" :anime="a" @open="router.push(`/anime/${a.id}`)" />
            </div>
            <el-empty v-else-if="!tabLoading" description="TA 还没有贡献过动漫" :image-size="80" />
            <div v-if="animesTotal > pageSize" class="pager">
              <el-pagination
                v-model:current-page="animePage"
                :page-size="pageSize"
                :total="animesTotal"
                layout="total, prev, pager, next"
                background
                small
                @current-change="loadAnimes"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </template>
    <el-empty v-else-if="!loading" description="用户不存在" />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { ROLE_LABEL, ROLE } from '@/constants'
import { avatarUrl } from '@/utils/avatar'
import PostCard from '@/components/PostCard.vue'
import AnimeCard from '@/components/AnimeCard.vue'

const route = useRoute()
const router = useRouter()
const store = useUserStore()

const profile = ref(null)
const loading = ref(false)
const tabLoading = ref(false)
const activeTab = ref('posts')

const posts = ref([])
const postsTotal = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const animes = ref([])
const animesTotal = ref(0)
const animePage = ref(1)

const isSelf = computed(() => store.user?.id === profile.value?.id)
const roleLabel = computed(() => ROLE_LABEL[profile.value?.role] || '')
const roleTagType = computed(() => {
  if (profile.value?.role === ROLE.SUPER_ADMIN) return 'danger'
  if (profile.value?.role === ROLE.ADMIN) return 'warning'
  return 'primary'
})

async function loadProfile() {
  loading.value = true
  try {
    profile.value = await userApi.profile(route.params.id)
    activeTab.value = 'posts'
    loadPosts()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

async function loadPosts() {
  tabLoading.value = true
  try {
    const data = await userApi.userPosts(route.params.id, { pageNum: pageNum.value, pageSize: pageSize.value })
    posts.value = data.records || []
    postsTotal.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    tabLoading.value = false
  }
}

async function loadAnimes() {
  tabLoading.value = true
  try {
    const data = await userApi.userAnimes(route.params.id, { pageNum: animePage.value, pageSize: pageSize.value })
    animes.value = data.records || []
    animesTotal.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    tabLoading.value = false
  }
}

watch(
  () => activeTab.value,
  (tab) => {
    if (tab === 'animes') loadAnimes()
    else loadPosts()
  },
)

watch(() => route.params.id, loadProfile)
onMounted(loadProfile)
</script>

<style scoped>
.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px 28px;
}
.avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #2aa3ff, #7c5cff);
}
.avatar-fallback {
  font-size: 34px;
  font-weight: 600;
  color: #fff;
}
.info {
  flex: 1;
  min-width: 0;
}
.name-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.name {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
}
.username {
  margin: 4px 0 0;
  color: var(--text-light);
  font-size: 13px;
}
.bio {
  margin: 10px 0 0;
  color: var(--text);
  font-size: 14px;
  line-height: 1.7;
  word-break: break-word;
}
.join {
  margin: 8px 0 0;
}
.stats {
  display: flex;
  gap: 24px;
  flex-shrink: 0;
}
.stat {
  text-align: center;
}
.stat b {
  display: block;
  font-size: 22px;
  color: var(--primary);
}
.stat span {
  font-size: 13px;
  color: var(--text-light);
}
.profile-tabs {
  margin-top: 8px;
}
.tab-body {
  min-height: 120px;
}
.post-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.anime-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}
.pager {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
@media (max-width: 768px) {
  .profile-card {
    flex-direction: column;
    text-align: center;
  }
  .name-row {
    justify-content: center;
  }
}
</style>