<template>
  <div class="rich-toolbar">
    <el-tooltip content="加粗">
      <el-button size="small" @click="emit('wrap', { before: '<b>', after: '</b>', fallback: '加粗文字' })"><b>B</b></el-button>
    </el-tooltip>
    <el-tooltip content="斜体">
      <el-button size="small" @click="emit('wrap', { before: '<i>', after: '</i>', fallback: '斜体文字' })"><i>I</i></el-button>
    </el-tooltip>
    <el-tooltip content="下划线">
      <el-button size="small" @click="emit('wrap', { before: '<u>', after: '</u>', fallback: '下划线文字' })"><u>U</u></el-button>
    </el-tooltip>
    <el-tooltip content="标题">
      <el-button size="small" @click="emit('wrap', { before: '<h3>', after: '</h3>', fallback: '标题' })">H</el-button>
    </el-tooltip>
    <el-tooltip content="文字颜色">
      <el-color-picker size="small" :predefine="predefineColors" @change="onColor" />
    </el-tooltip>
    <el-tooltip content="字号">
      <el-select size="small" style="width: 92px" placeholder="字号" @change="onSize">
        <el-option v-for="n in sizes" :key="n" :label="`${n}px`" :value="n" />
      </el-select>
    </el-tooltip>
    <el-tooltip content="引用块">
      <el-button size="small" @click="emit('wrap', { before: '<blockquote>\n', after: '\n</blockquote>', fallback: '引用内容' })">引用</el-button>
    </el-tooltip>
    <el-tooltip content="分割线">
      <el-button size="small" @click="emit('wrap', { before: '\n<hr>\n', after: '', fallback: '' })">—</el-button>
    </el-tooltip>
    <span class="hint">支持 &lt;b&gt;加粗&lt;/b&gt;、&lt;span style=&quot;color:#e5484d&quot;&gt;彩色&lt;/span&gt; 等标签</span>
  </div>
</template>

<script setup>
const emit = defineEmits(['wrap', 'color', 'size'])

const sizes = [14, 16, 18, 20, 24, 28]
const predefineColors = ['#e5484d', '#ff8a00', '#16a34a', '#2aa3ff', '#7c5cff', '#2c3e50']

function onColor(color) {
  if (color) emit('color', color)
}

function onSize(size) {
  if (size) emit('size', size)
}
</script>

<style scoped>
.rich-toolbar {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
  margin-bottom: 6px;
}
.rich-toolbar :deep(.el-color-picker) {
  vertical-align: middle;
}
.hint {
  font-size: 12px;
  color: var(--text-light);
  margin-left: 4px;
}
</style>