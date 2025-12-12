# HTTP 安全標頭和緩存策略改進

## 已解決的問題

### ✅ 1. 添加 `X-Content-Type-Options` 標頭
**問題**: Response should include 'x-content-type-options' header.

**解決方案**:
```nginx
add_header X-Content-Type-Options "nosniff" always;
```

**作用**: 防止瀏覽器進行 MIME 類型嗅探，強制瀏覽器遵守 Content-Type 標頭。這可以防止某些類型的 XSS 攻擊。

---

### ✅ 2. 移除 `-webkit-text-size-adjust`
**問題**: '-webkit-text-size-adjust' is not supported by Chrome, Chrome Android, Edge 79+, Firefox, Safari.

**解決方案**: 
- 未在配置中使用此屬性
- 如果需要文字大小調整，應使用標準的 `text-size-adjust` 屬性

**注意**: Tailwind CSS 預設不使用此屬性，因此無需額外處理。

---

### ⚠️ 3. 移除 `theme-color` Meta 標籤
**問題**: 'meta[name=theme-color]' is not supported by Firefox.

**解決方案**:
```typescript
// 已從 nuxt.config.ts 中移除
// { name: 'theme-color', content: '#2563eb' },
```

**影響**: 
- ✅ 提高跨瀏覽器兼容性
- ⚠️ Chrome/Safari 移動版將不會顯示自定義主題顏色
- 這是可接受的權衡，因為主題顏色是視覺增強而非核心功能

---

### ✅ 4. 添加 `Cache-Control` 標頭
**問題**: A 'cache-control' header is missing or empty.

**解決方案**:

#### HTML 文件（不緩存）:
```nginx
location ~* \.html$ {
    add_header Cache-Control "no-cache, no-store, must-revalidate" always;
    add_header Pragma "no-cache" always;
    add_header Expires "0" always;
}
```

#### 靜態資源（長期緩存）:
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
    expires 1y;
    add_header Cache-Control "public, immutable" always;
}
```

#### API 請求（不緩存）:
```nginx
location /api/ {
    add_header Cache-Control "no-cache, no-store, must-revalidate" always;
}
```

**作用**: 
- HTML 文件始終獲取最新版本
- 靜態資源可以長期緩存（1年）
- API 響應不緩存，確保數據新鮮度

---

### ✅ 5. 移除 `X-XSS-Protection` 標頭
**問題**: Response should not include unneeded headers: x-xss-protection

**解決方案**: 
- 未在配置中添加此標頭
- 此標頭已被現代瀏覽器棄用，應使用 Content-Security-Policy 代替

**原因**: 
- `X-XSS-Protection` 在某些情況下可能引入安全漏洞
- 現代瀏覽器已移除此功能
- Content-Security-Policy 提供更好的保護

---

## 額外添加的安全標頭

### 1. X-Frame-Options
```nginx
add_header X-Frame-Options "SAMEORIGIN" always;
```
**作用**: 防止點擊劫持攻擊，只允許同源頁面嵌入。

### 2. Referrer-Policy
```nginx
add_header Referrer-Policy "strict-origin-when-cross-origin" always;
```
**作用**: 控制 Referer 標頭的發送方式，保護用戶隱私。

### 3. Permissions-Policy
```nginx
add_header Permissions-Policy "geolocation=(), microphone=(), camera=()" always;
```
**作用**: 控制瀏覽器功能的訪問權限。

### 4. Content-Security-Policy
```nginx
add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:; connect-src 'self' http://localhost:8080 http://backend:8080;" always;
```
**作用**: 
- 防止 XSS 攻擊
- 控制資源加載來源
- 提供多層安全防護

**注意**: 
- `'unsafe-inline'` 和 `'unsafe-eval'` 是為了支援 Nuxt/Vue 的開發模式
- 生產環境建議移除這些不安全的選項並使用 nonce 或 hash

---

## 緩存策略說明

### HTML 文件
- **策略**: 不緩存
- **原因**: 確保用戶始終獲取最新的應用版本
- **標頭**: `no-cache, no-store, must-revalidate`

### JavaScript/CSS
- **策略**: 長期緩存（1年）
- **原因**: 這些文件有版本哈希，內容變更時文件名會改變
- **標頭**: `public, immutable`

### 圖片和字體
- **策略**: 長期緩存（1年）
- **原因**: 靜態資源很少變更
- **標頭**: `public, immutable`

### API 響應
- **策略**: 不緩存
- **原因**: 確保數據新鮮度
- **標頭**: `no-cache, no-store, must-revalidate`

---

## 測試建議

### 1. 檢查 HTTP 標頭
使用瀏覽器開發者工具或 curl:
```bash
# 檢查主頁標頭
curl -I http://localhost:3000/

# 檢查 API 標頭
curl -I http://localhost:3000/api/products
```

### 2. 安全標頭檢查工具
- [Security Headers](https://securityheaders.com/)
- [Mozilla Observatory](https://observatory.mozilla.org/)

### 3. 緩存驗證
1. 首次訪問頁面
2. 刷新頁面（F5）
3. 強制刷新（Ctrl+F5）
4. 檢查 Network 面板中的 "Size" 列
   - 應該看到 "(from disk cache)" 或 "(from memory cache)"

---

## 瀏覽器兼容性

### 支援的標頭
| 標頭 | Chrome | Firefox | Safari | Edge |
|------|--------|---------|--------|------|
| X-Content-Type-Options | ✅ | ✅ | ✅ | ✅ |
| X-Frame-Options | ✅ | ✅ | ✅ | ✅ |
| Referrer-Policy | ✅ | ✅ | ✅ | ✅ |
| Content-Security-Policy | ✅ | ✅ | ✅ | ✅ |
| Cache-Control | ✅ | ✅ | ✅ | ✅ |
| Permissions-Policy | ✅ | ✅ | ⚠️ | ✅ |

### 已移除的標頭
| 標頭 | 狀態 | 原因 |
|------|------|------|
| X-XSS-Protection | ❌ 已棄用 | 現代瀏覽器已移除，可能引入漏洞 |
| theme-color (meta) | ⚠️ 部分支援 | Firefox 不支援 |

---

## 生產環境建議

### 1. 強化 Content-Security-Policy
```nginx
# 移除 unsafe-inline 和 unsafe-eval
add_header Content-Security-Policy "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data: https:; font-src 'self' data:; connect-src 'self' https://api.yourdomain.com;" always;
```

### 2. 啟用 HTTPS
```nginx
# 添加 HSTS 標頭
add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload" always;
```

### 3. 添加 SRI (Subresource Integrity)
在 Nuxt 配置中為外部資源添加 integrity 屬性。

### 4. 實施 CORS 策略
```nginx
# 如果需要跨域請求
add_header Access-Control-Allow-Origin "https://yourdomain.com" always;
add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS" always;
add_header Access-Control-Allow-Headers "Content-Type, Authorization" always;
```

---

## 相關資源

- [OWASP Secure Headers Project](https://owasp.org/www-project-secure-headers/)
- [MDN HTTP Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)
- [Content Security Policy Reference](https://content-security-policy.com/)
- [HTTP Caching](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching)
