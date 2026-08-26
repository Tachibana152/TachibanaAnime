<template>
  <div class="login-page">
    <div class="login-card card">
      <div class="brand" @click="router.push('/home')">
        <el-icon :size="28" color="#2aa3ff"><Film /></el-icon>
        <span>Tachibana 动画世界</span>
      </div>
      <el-tabs v-model="mode" stretch>
        <el-tab-pane label="登录" name="login" />
        <el-tab-pane label="注册" name="register" />
      </el-tabs>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" @keyup.enter="submit">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item v-if="mode === 'register'" label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="请再次输入密码" :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item v-if="mode === 'register'" label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称（选填）" :prefix-icon="Avatar" />
        </el-form-item>
        <el-button type="primary" class="submit" :loading="loading" @click="submit">
          {{ mode === 'login' ? '登 录' : '注 册' }}
        </el-button>
      </el-form>

      <p class="tips text-muted">演示账号：admin / 123456（超级管理员），test / 123456（普通用户）</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Avatar } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const formRef = ref(null)
const mode = ref('login')
const loading = ref(false)
const form = reactive({ username: '', password: '', confirmPassword: '', nickname: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次输入的密码不一致'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
}

watch(mode, () => {
  form.confirmPassword = ''
})

async function submit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (mode.value === 'login') {
      await store.login({ username: form.username, password: form.password })
      ElMessage.success('登录成功')
      router.push(route.query.redirect || '/home')
    } else {
      await store.register({ username: form.username, password: form.password, nickname: form.nickname })
      ElMessage.success('注册成功，请登录')
      mode.value = 'login'
    }
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(42, 163, 255, 0.1), rgba(255, 24, 251, 0.08)), var(--bg);
}
.login-card {
  width: 400px;
  padding: 36px 32px 24px;
}
.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
  cursor: pointer;
  background: linear-gradient(90deg, #2aa3ff, #ff18fb);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.submit {
  width: 100%;
  margin-top: 4px;
}
.tips {
  margin-top: 16px;
  text-align: center;
}
</style>