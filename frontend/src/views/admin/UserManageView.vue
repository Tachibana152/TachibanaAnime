<template>
  <div class="page-container">
    <div class="section-header">
      <h2 class="page-title" style="margin-bottom: 0">用户管理</h2>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索用户名 / 昵称…" clearable style="width: 260px" @keyup.enter="onSearch" @clear="onSearch" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="openAuditList">
        头像审核
        <el-badge v-if="auditCount > 0" :value="auditCount" :max="99" class="audit-badge" />
      </el-button>
    </div>

    <el-table v-loading="loading" :data="list" border stripe class="mt-16">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="头像" width="84">
        <template #default="{ row }">
          <el-badge is-dot :hidden="!row.avatarPending" type="danger" class="avatar-badge">
            <el-avatar :size="44" :src="row.avatar" @click="openAudit(row)">
              <template #error>{{ (row.nickname || row.username)?.[0] || 'U' }}</template>
            </el-avatar>
          </el-badge>
        </template>
      </el-table-column>
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

    <!-- 待审核头像列表 -->
    <el-dialog v-model="auditListVisible" title="待审核头像" width="560px">
      <el-table :data="audits" border stripe max-height="420">
        <el-table-column prop="nickname" label="用户" min-width="120">
          <template #default="{ row }">{{ row.nickname || row.username }}</template>
        </el-table-column>
        <el-table-column label="现头像" width="70">
          <template #default="{ row }"><el-avatar :size="36" :src="row.avatar" /></template>
        </el-table-column>
        <el-table-column label="待审头像" width="70">
          <template #default="{ row }"><el-avatar :size="36" :src="row.avatarPending" /></template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="openAudit(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!audits.length" description="暂无待审核头像" />
    </el-dialog>

    <!-- 单个头像审核 -->
    <el-dialog v-model="auditVisible" title="头像审核" width="520px">
      <div class="audit-user">
        <el-avatar :size="56" :src="auditUser?.avatar" class="audit-avatar" />
        <div>
          <b>{{ auditUser?.nickname || auditUser?.username }}</b>
          <p class="text-muted">@{{ auditUser?.username }}</p>
        </div>
      </div>
      <div class="audit-compare">
        <div class="audit-col">
          <p class="text-muted">当前头像</p>
          <el-avatar :size="88" :src="auditUser?.avatar">
            <template #error>{{ auditUser?.nickname?.[0] || 'U' }}</template>
          </el-avatar>
        </div>
        <el-icon class="audit-arrow" :size="26" color="#8a94a6"><Right /></el-icon>
        <div class="audit-col">
          <p class="text-muted">待审核头像</p>
          <el-avatar :size="88" :src="auditUser?.avatarPending">
            <template #error>{{ auditUser?.nickname?.[0] || 'U' }}</template>
          </el-avatar>
        </div>
      </div>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="danger" plain :loading="auditApproving" @click="review(false)">驳回</el-button>
        <el-button type="primary" :loading="auditApproving" @click="review(true)">通过</el-button>
      </template>
    </el-dialog>
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

// 头像审核
const auditListVisible = ref(false)
const auditVisible = ref(false)
const audits = ref([])
const auditCount = ref(0)
const auditUser = ref(null)
const auditApproving = ref(false)

async function loadAudits() {
  try {
    audits.value = await userApi.listAvatarAudits()
    auditCount.value = audits.value.length
  } catch (e) {
    ElMessage.error(e.message)
  }
}

function openAuditList() {
  auditListVisible.value = true
  loadAudits()
}

function openAudit(row) {
  if (!row.avatarPending) return ElMessage.info('该用户没有待审核的头像')
  auditUser.value = row
  auditVisible.value = true
}

async function review(approve) {
  if (!auditUser.value) return
  auditApproving.value = true
  try {
    await userApi.reviewAvatar(auditUser.value.id, approve)
    ElMessage.success(approve ? '已通过，头像已生效' : '已驳回')
    auditVisible.value = false
    load()
    loadAudits()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    auditApproving.value = false
  }
}

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

onMounted(() => {
  load()
  loadAudits()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
}
.audit-badge {
  margin-left: 4px;
}
.avatar-badge {
  cursor: pointer;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
.audit-user {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.audit-compare {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}
.audit-col {
  text-align: center;
}
.audit-arrow {
  margin-top: 20px;
}
</style>