// 关键词高亮辅助
export function highlightText(text, keyword) {
  if (!keyword || !text) return text
  const esc = text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
  const safeKw = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const idx = esc.toLowerCase().indexOf(safeKw.toLowerCase())
  if (idx < 0) return esc
  const start = esc.slice(0, idx)
  const hit = esc.slice(idx, idx + keyword.length)
  const end = esc.slice(idx + keyword.length)
  return `${start}<span class="highlight">${hit}</span>${end}`
}