<template>
  <div class="main-layout">
    <header class="navbar">
      <div class="navbar-inner">
        <div class="brand" @click="router.push('/home')">
          <el-icon :size="26" color="#2aa3ff"><Film /></el-icon>
          <span class="brand-text">Tachibana Anime</span>
        </div>

        <nav class="nav-menu">
          <router-link to="/home" class="nav-item" active-class="active">首页</router-link>
          <router-link to="/forum" class="nav-item" active-class="active">论坛</router-link>
          <router-link v-if="store.isLoggedIn" to="/post/edit" class="nav-item" active-class="active">发帖</router-link>
          <router-link v-if="store.isLoggedIn" to="/myposts" class="nav-item" active-class="active">我的帖子</router-link>

          <el-dropdown v-if="store.isAdmin" trigger="hover" class="admin-drop">
            <span class="nav-item admin-entry">
              管理后台
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/admin/anime')">动漫管理</el-dropdown-item>
                <el-dropdown-item @click="router.push('/admin/posts')">帖子管理 / 审核</el-dropdown-item>
                <el-dropdown-item v-if="store.isSuperAdmin" @click="router.push('/admin/users')">用户管理</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </nav>

        <div class="nav-right">
          <template v-if="store.isLoggedIn">
            <el-tag :type="roleTagType" size="small" effect="light">{{ roleLabel }}</el-tag>
            <el-dropdown trigger="click">
              <span class="user-entry">
                <el-avatar :size="30" :src="avatarUrl(store.user)" class="avatar">
                  <template #error>{{ store.user?.nickname?.[0] || 'U' }}</template>
                </el-avatar>
                <span class="username">{{ store.user?.nickname || store.user?.username }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push(`/user/${store.user?.id}`)">个人主页</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/myposts')">我的帖子</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/settings')">账号设置</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" round @click="router.push('/login')">登录 / 注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <p>站长Tachibana制作</p>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { ROLE, ROLE_LABEL } from '@/constants'
import { avatarUrl } from '@/utils/avatar'

const router = useRouter()
const store = useUserStore()

const roleLabel = computed(() => ROLE_LABEL[store.role] || '')
const roleTagType = computed(() => {
  if (store.role === ROLE.SUPER_ADMIN) return 'danger'
  if (store.role === ROLE.ADMIN) return 'warning'
  return 'primary'
})

async function handleLogout() {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  store.logout()
  ElMessage.success('已退出登录')
  router.push('/home')
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid var(--border);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  gap: 24px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-weight: 700;
  font-size: 18px;
  color: var(--text);
  white-space: nowrap;
}
.brand-text {
  background: linear-gradient(90deg, #2aa3ff, #ff18fb);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.nav-menu {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
}
.nav-item {
  padding: 6px 14px;
  border-radius: 8px;
  font-size: 15px;
  color: var(--text);
  transition: all 0.2s;
  cursor: pointer;
}
.nav-item:hover {
  background: rgba(42, 163, 255, 0.08);
  color: var(--primary);
}
.nav-item.active {
  color: var(--primary);
  font-weight: 600;
  background: rgba(42, 163, 255, 0.1);
}
.admin-entry {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  outline: none;
}
.nav-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-entry {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  outline: none;
}
.avatar {
  background: linear-gradient(135deg, #2aa3ff, #7c5cff);
  color: #fff;
  font-weight: 600;
}
.username {
  font-size: 14px;
  color: var(--text);
}
.main-content {
  flex: 1;
}
.footer {
  text-align: center;
  padding: 24px;
  color: var(--text-light);
  font-size: 13px;
  border-top: 1px solid var(--border);
  background: #fff;
}
</style>