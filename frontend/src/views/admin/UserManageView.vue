<template>
  <div class="page-container">
    <div class="section-header">
      <h2 class="page-title" style="margin-bottom: 0">用户管理</h2>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索用户名 / 昵称…" clearable style="width: 260px" @keyup.enter="onSearch" @clear="onSearch" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
    </div>

    <el-table v-loading="loading" :data="list" border stripe class="mt-16">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="nickname" label="昵称" min-width="120" />
      <el-table-column label="角色" width="150">
        <template #default="{ row }">
          <el-select :model-value="row.role" size="small" :disabled="row.id === store.user?.id" @change="(v) => onChangeRole(row, v)">
            <el-option label="普通用户" value="USER" />
            <el-option label="管理员" value="ADMIN" />
            <el-option label="超级管理员" value="SUPER_ADMIN" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" min-width="150" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.id !== store.user?.id" link :type="row.status === 1 ? 'warning' : 'success'" @click="onToggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button v-if="row.id !== store.user?.id" link type="danger" @click="onDelete(row)">删除</el-button>
          <span v-else class="text-muted">当前账号</span>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { ROLE_LABEL } from '@/constants'

const store = useUserStore()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const keyword = ref('')

async function load() {
  loading.value = true
  try {
    const data = await userApi.listUsers({
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

async function onChangeRole(row, role) {
  await ElMessageBox.confirm(`确定将「${row.username}」的角色改为「${ROLE_LABEL[role]}」吗？`, '提示', { type: 'warning' })
  try {
    await userApi.updateRole(row.id, role)
    ElMessage.success('角色已更新')
    load()
  } catch (e) {
    ElMessage.error(e.message)
    load()
  }
}

async function onToggleStatus(row) {
  const action = row.status === 1 ? '禁用' : '启用'
  await ElMessageBox.confirm(`确定${action}用户「${row.username}」吗？`, '提示', { type: 'warning' })
  try {
    await userApi.updateStatus(row.id, row.status === 1 ? 0 : 1)
    ElMessage.success(`已${action}`)
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除用户「${row.username}」吗？其帖子将一并处理。`, '警告', { type: 'warning' })
  try {
    await userApi.removeUser(row.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 10px;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>