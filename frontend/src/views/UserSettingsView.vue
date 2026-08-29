<template>
  <div class="page-container" style="max-width: 720px">
    <h2 class="page-title">账号设置</h2>

    <el-form :model="form" ref="formRef" label-position="top" class="settings-form card">
      <!-- 头像 -->
      <el-form-item label="头像">
        <div class="avatar-row">
          <el-avatar :size="80" :src="currentAvatar" class="avatar">
            <template #error><span class="avatar-fallback">{{ (form.nickname || store.user?.username)?.[0] || 'U' }}</span></template>
          </el-avatar>
          <div class="avatar-ops">
            <el-upload :show-file-list="false" accept="image/*" :before-upload="beforeAvatarUpload" :http-request="uploadAvatar">
              <el-button type="primary" plain size="small">
                <el-icon><Upload /></el-icon> 更换头像
              </el-button>
            </el-upload>
            <p class="text-muted hint">支持 jpg/png/gif/webp，不超过 1MB</p>
            <p v-if="pendingAvatar" class="pending-tip">
              <el-icon><Clock /></el-icon> 新头像已提交，等待超级管理员审核
            </p>
          </div>
        </div>
      </el-form-item>

      <!-- 昵称 / 简介 -->
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" maxlength="20" show-word-limit placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item label="个人简介">
        <el-input v-model="form.bio" type="textarea" :rows="4" maxlength="200" show-word-limit placeholder="介绍一下自己吧（选填）" />
      </el-form-item>

      <div class="actions">
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api/auth'
import { fileApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { AVATAR_MAX_SIZE } from '@/constants'
import { avatarUrl } from '@/utils/avatar'

const store = useUserStore()
const formRef = ref(null)
const saving = ref(false)

const form = reactive({ nickname: '', bio: '' })
const pendingAvatar = ref('')

const currentAvatar = computed(() => avatarUrl(store.user))

function beforeAvatarUpload(file) {
  if (file.size > AVATAR_MAX_SIZE) {
    ElMessage.warning('头像大小不能超过 1MB')
    return false
  }
  if (!/\.(jpg|jpeg|png|gif|webp)$/i.test(file.name)) {
    ElMessage.warning('仅支持 jpg/jpeg/png/gif/webp 格式')
    return false
  }
  return true
}

async function uploadAvatar(option) {
  try {
    const res = await fileApi.upload(option.file, 'avatar')
    const user = await authApi.submitAvatar(res.url)
    pendingAvatar.value = user.avatarPending || ''
    store.setAuth(store.token, user)
    ElMessage.success('头像已提交，等待超级管理员审核')
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function save() {
  if (!form.nickname.trim()) return ElMessage.warning('昵称不能为空')
  saving.value = true
  try {
    const user = await authApi.updateProfile({ nickname: form.nickname.trim(), bio: form.bio.trim() })
    store.setAuth(store.token, user)
    ElMessage.success('保存成功')
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  try {
    const user = await store.fetchMe()
    form.nickname = user.nickname || user.username || ''
    form.bio = user.bio || ''
    pendingAvatar.value = user.avatarPending || ''
  } catch (e) {
    ElMessage.error(e.message)
  }
})
</script>

<style scoped>
.settings-form {
  padding: 24px 28px;
}
.avatar-row {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}
.avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #2aa3ff, #7c5cff);
}
.avatar-fallback {
  font-size: 30px;
  font-weight: 600;
  color: #fff;
}
.avatar-ops {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: flex-start;
}
.hint {
  margin: 0;
}
.pending-tip {
  margin: 0;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #b8860b;
  background: rgba(230, 162, 60, 0.1);
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 13px;
}
.actions {
  margin-top: 8px;
  text-align: right;
}
</style>