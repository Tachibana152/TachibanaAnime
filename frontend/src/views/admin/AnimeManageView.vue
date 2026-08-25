<template>
  <div class="page-container">
    <div class="section-header">
      <h2 class="page-title" style="margin-bottom: 0">动漫管理</h2>
      <el-button type="primary" round @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增动漫
      </el-button>
    </div>

    <div class="toolbar">
      <el-select v-model="query.category" placeholder="全部分类" clearable style="width: 160px" @change="onSearch">
        <el-option label="一月新番" value="NEW" />
        <el-option label="经典动画" value="CLASSIC" />
      </el-select>
      <el-input v-model="query.keyword" placeholder="搜索标题/原作/制作…" clearable style="width: 260px" @keyup.enter="onSearch" @clear="onSearch" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
    </div>

    <el-table v-loading="loading" :data="list" border stripe class="mt-16">
      <el-table-column label="封面" width="80">
        <template #default="{ row }">
          <el-image :src="row.cover" fit="cover" style="width: 48px; height: 64px; border-radius: 4px">
            <template #error><div class="thumb">无</div></template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
      <el-table-column prop="category" label="分类" width="90">
        <template #default="{ row }">
          <el-tag :type="row.category === 'NEW' ? 'danger' : 'warning'" size="small">
            {{ row.category === 'NEW' ? '新番' : '经典' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="production" label="制作" min-width="140" show-overflow-tooltip />
      <el-table-column prop="episodes" label="话数" width="70" />
      <el-table-column prop="airDate" label="放送开始" width="130" />
      <el-table-column prop="viewCount" label="浏览" width="80" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑动漫' : '新增动漫'" width="680px" top="6vh">
      <el-form :model="form" label-width="90px">
        <div class="form-row">
          <el-form-item label="标题" required>
            <el-input v-model="form.title" placeholder="中文标题" />
          </el-form-item>
          <el-form-item label="日文名">
            <el-input v-model="form.titleJp" placeholder="日文原名" />
          </el-form-item>
        </div>
        <div class="form-row">
          <el-form-item label="分类" required>
            <el-select v-model="form.category" style="width: 100%">
              <el-option label="一月新番" value="NEW" />
              <el-option label="经典动画" value="CLASSIC" />
            </el-select>
          </el-form-item>
          <el-form-item label="封面">
            <div class="cover-upload">
              <el-upload
                :show-file-list="false"
                :http-request="uploadCover"
                accept="image/*"
              >
                <img v-if="form.cover" :src="form.cover" class="cover-preview" />
                <div v-else class="cover-placeholder">
                  <el-icon><Plus /></el-icon>
                  <span>上传封面</span>
                </div>
              </el-upload>
              <el-input v-model="form.cover" placeholder="或直接填写图片 URL" style="flex: 1" />
            </div>
          </el-form-item>
        </div>
        <div class="form-row">
          <el-form-item label="原作">
            <el-input v-model="form.original" />
          </el-form-item>
          <el-form-item label="导演">
            <el-input v-model="form.director" />
          </el-form-item>
        </div>
        <div class="form-row">
          <el-form-item label="话数">
            <el-input-number v-model="form.episodes" :min="1" style="width: 100%" />
          </el-form-item>
          <el-form-item label="放送开始">
            <el-input v-model="form.airDate" placeholder="如 2026年1月16日" />
          </el-form-item>
        </div>
        <el-form-item label="制作公司">
          <el-input v-model="form.production" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.synopsis" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="分集/内容介绍，支持换行" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { animeApi } from '@/api/anime'
import { fileApi } from '@/api/user'

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const query = reactive({ category: '', keyword: '' })

const emptyForm = () => ({
  id: null, title: '', titleJp: '', category: 'NEW', cover: '',
  original: '', director: '', writer: '', episodes: 1, airDate: '', airWeekday: '', production: '',
  synopsis: '', content: '',
})
const form = reactive(emptyForm())

async function load() {
  loading.value = true
  try {
    const data = await animeApi.list({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      category: query.category || undefined,
      keyword: query.keyword || undefined,
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

function openDialog(row) {
  Object.assign(form, row ? { ...row } : emptyForm())
  dialogVisible.value = true
}

async function uploadCover(option) {
  try {
    const res = await fileApi.upload(option.file)
    form.cover = res.url
    ElMessage.success('封面上传成功')
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function save() {
  if (!form.title) return ElMessage.warning('请输入标题')
  saving.value = true
  try {
    if (form.id) {
      await animeApi.update(form.id, { ...form })
      ElMessage.success('已更新')
    } else {
      await animeApi.create({ ...form })
      ElMessage.success('已新增')
    }
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '警告', { type: 'warning' })
  try {
    await animeApi.remove(row.id)
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
.thumb {
  width: 48px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  color: var(--text-light);
  font-size: 12px;
}
.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
.form-row {
  display: flex;
  gap: 16px;
}
.form-row .el-form-item {
  flex: 1;
}
.cover-upload {
  display: flex;
  align-items: center;
  gap: 10px;
}
.cover-preview {
  width: 90px;
  height: 120px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
}
.cover-placeholder {
  width: 90px;
  height: 120px;
  border: 1px dashed #c0c4cc;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--text-light);
  cursor: pointer;
}
</style>