import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ROLE } from '@/constants'

const MainLayout = () => import('@/layout/MainLayout.vue')

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/home' },
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/HomeView.vue'),
        meta: { title: '首页' },
      },
      {
        path: 'anime/:id',
        name: 'animeDetail',
        component: () => import('@/views/AnimeDetailView.vue'),
        meta: { title: '动画详情' },
      },
      {
        path: 'forum',
        name: 'forum',
        component: () => import('@/views/ForumView.vue'),
        meta: { title: '论坛' },
      },
      {
        path: 'post/:id',
        name: 'postDetail',
        component: () => import('@/views/PostDetailView.vue'),
        meta: { title: '帖子详情' },
      },
      {
        path: 'post/edit',
        name: 'postCreate',
        component: () => import('@/views/PostEditView.vue'),
        meta: { title: '发帖', requiresAuth: true },
      },
      {
        path: 'post/edit/:id',
        name: 'postEdit',
        component: () => import('@/views/PostEditView.vue'),
        meta: { title: '编辑帖子', requiresAuth: true },
      },
      {
        path: 'myposts',
        name: 'myPosts',
        component: () => import('@/views/MyPostsView.vue'),
        meta: { title: '我的帖子', requiresAuth: true },
      },
      {
        path: 'user/:id',
        name: 'userProfile',
        component: () => import('@/views/UserProfileView.vue'),
        meta: { title: '个人主页', requiresAuth: true },
      },
      {
        path: 'settings',
        name: 'settings',
        component: () => import('@/views/UserSettingsView.vue'),
        meta: { title: '账号设置', requiresAuth: true },
      },
      {
        path: 'admin/anime',
        name: 'adminAnime',
        component: () => import('@/views/admin/AnimeManageView.vue'),
        meta: { title: '动漫管理', requiresAuth: true, roles: [ROLE.ADMIN, ROLE.SUPER_ADMIN] },
      },
      {
        path: 'admin/posts',
        name: 'adminPosts',
        component: () => import('@/views/admin/PostManageView.vue'),
        meta: { title: '帖子管理', requiresAuth: true, roles: [ROLE.ADMIN, ROLE.SUPER_ADMIN] },
      },
      {
        path: 'admin/users',
        name: 'adminUsers',
        component: () => import('@/views/admin/UserManageView.vue'),
        meta: { title: '用户管理', requiresAuth: true, roles: [ROLE.SUPER_ADMIN] },
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录', public: true },
  },
  { path: '/:pathMatch(.*)*', redirect: '/home' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} - Tachibana Anime` : 'Tachibana Anime'
  const store = useUserStore()

  if (to.meta.public) {
    return true
  }
  if (!store.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.roles && !to.meta.roles.includes(store.role)) {
    return { path: '/home' }
  }
  return true
})

export default router