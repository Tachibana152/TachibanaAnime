// 富文本渲染辅助：白名单 HTML 标签 + DOMPurify 清洗 + 标签感知搜索高亮
import DOMPurify from 'dompurify'

const ALLOWED_TAGS = [
  'b', 'strong', 'i', 'em', 'u', 's', 'mark', 'small', 'sub', 'sup', 'code', 'kbd',
  'font', 'span', 'p', 'br', 'hr', 'center', 'blockquote',
  'h1', 'h2', 'h3', 'h4', 'ul', 'ol', 'li', 'pre',
]

// 仅放行安全的行内样式属性
const SAFE_STYLE_PROPS = new RegExp(
  '^(color|background-color|background|font-size|font-weight|font-style|' +
    'text-align|text-decoration|line-height|margin|padding|border)$',
  'i',
)

const DANGEROUS_CSS = /url\s*\(|javascript:|expression\s*\(|@import|behavior/i

DOMPurify.addHook('uponSanitizeAttribute', (node, data) => {
  if (data.attrName !== 'style') return
  const safe = []
  for (const decl of String(data.attrValue || '').split(';')) {
    const m = decl.match(/^\s*([\w-]+)\s*:\s*(.+?)\s*$/)
    if (!m) continue
    const prop = m[1].trim().toLowerCase()
    const val = m[2].trim()
    if (!SAFE_STYLE_PROPS.test(prop) || DANGEROUS_CSS.test(val)) continue
    safe.push(`${prop}: ${val}`)
  }
  data.attrValue = safe.join('; ')
  if (!safe.length) {
    node.removeAttribute('style')
    data.attrValue = ''
  }
})

// 标签感知高亮：按 <...> 切分，仅替换文本段，避免破坏标签/属性
function highlightHtml(html, keyword) {
  if (!keyword || !html) return html
  const esc = String(keyword).replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const re = new RegExp(esc, 'gi')
  return html
    .split(/(<[^>]+>)/g)
    .map((seg) => (/^<[^>]+>$/.test(seg) ? seg : seg.replace(re, '<span class="highlight">$&</span>')))
    .join('')
}

/**
 * 渲染富文本：换行转 <br> -> DOMPurify 白名单清洗 -> 关键词高亮
 * @param {string} text 原文（可含白名单 HTML 标签）
 * @param {string} [keyword] 搜索关键词
 * @returns {string} 安全 HTML，可直接用于 v-html
 */
export function renderRich(text, keyword) {
  if (!text) return ''
  const html = String(text).replace(/\n/g, '<br>')
  const safe = DOMPurify.sanitize(html, {
    ALLOWED_TAGS,
    ALLOWED_ATTR: ['style', 'color', 'size', 'face'],
  })
  return highlightHtml(safe, keyword)
}