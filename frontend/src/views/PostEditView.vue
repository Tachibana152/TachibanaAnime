<template>
  <div class="page-container" style="max-width: 860px">
    <h2 class="page-title">{{ isEdit ? '编辑帖子' : '发帖' }}</h2>

    <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" maxlength="60" show-word-limit placeholder="请输入帖子标题" />
      </el-form-item>
      <el-form-item label="正文" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="12"
          maxlength="5000"
          show-word-limit
          placeholder="请输入正文内容，支持多段文字（空行分段）…"
        />
      </el-form-item>
      <el-form-item label="来源链接（选填）">
        <el-input v-model="form.sourceUrl" placeholder="https://bgm.tv/..." clearable />
      </el-form-item>

      <div class="actions">
        <el-button @click="router.back()">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">提交{{ isEdit ? '修改' : '' }}</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { forumApi } from '@/api/forum'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const saving = ref(false)
const isEdit = computed(() => !!route.params.id)

const form = reactive({ title: '', content: '', sourceUrl: '' })

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入正文', trigger: 'blur' }],
}

async function loadPost() {
  try {
    const mine = await forumApi.myPosts({ pageNum: 1, pageSize: 100 })
    const target = mine.records?.find((p) => p.id === Number(route.params.id))
    if (target) {
      form.title = target.title
      form.content = target.content
      form.sourceUrl = target.sourceUrl || ''
      return
    }
    const p = await forumApi.postDetail(route.params.id)
    if (p) {
      form.title = p.title
      form.content = p.content
      form.sourceUrl = p.sourceUrl || ''
    }
  } catch (e) {
    ElMessage.error('无法加载帖子：' + e.message)
  }
}

async function submit() {
  await formRef.value.validate()
  saving.value = true
  try {
    if (isEdit.value) {
      await forumApi.updatePost(route.params.id, { ...form })
      ElMessage.success('修改已提交，等待重新审核')
    } else {
      const res = await forumApi.createPost({ ...form })
      if (res?.autoApproved) {
        ElMessage.success('发布成功')
      } else {
        ElMessage.success('帖子已提交，等待管理员审核')
      }
    }
    router.push('/myposts')
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  if (isEdit.value) loadPost()
})
</script>

<style scoped>
.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>