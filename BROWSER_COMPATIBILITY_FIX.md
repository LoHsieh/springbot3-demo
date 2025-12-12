# 瀏覽器兼容性問題修復總結

## 問題 1: `-webkit-text-size-adjust` 警告

### 問題來源
Tailwind CSS 的 preflight（基礎樣式重置）會自動添加以下 CSS：
```css
html {
  -webkit-text-size-adjust: 100%;
}
```

這個屬性在現代瀏覽器中已被標準的 `text-size-adjust` 替代。

### 解決方案

#### 1. 創建自定義 CSS 文件
**文件**: `frontend/assets/css/main.css`

```css
/* 覆蓋 Tailwind preflight 中的過時屬性 */
html {
  /* 使用現代標準屬性替代 -webkit-text-size-adjust */
  text-size-adjust: 100%;
  -webkit-text-size-adjust: none; /* 明確禁用 */
  -moz-text-size-adjust: none;
  -ms-text-size-adjust: none;
}

/* 確保文字大小調整使用標準屬性 */
body {
  text-size-adjust: 100%;
}
```

#### 2. 在 Nuxt 配置中引入
**文件**: `frontend/nuxt.config.ts`

```typescript
export default defineNuxtConfig({
    // ...其他配置
    css: ['~/assets/css/main.css'],
    // ...
})
```

### 為什麼會出現這個問題？

1. **Tailwind CSS Preflight**: Tailwind 的 preflight 是一個基礎樣式重置層，它會添加一些跨瀏覽器的標準化樣式
2. **歷史遺留**: `-webkit-text-size-adjust` 是舊版 WebKit 瀏覽器使用的前綴屬性
3. **現代標準**: 現在應該使用無前綴的 `text-size-adjust`

### 瀏覽器支援

| 屬性 | Chrome | Firefox | Safari | Edge |
|------|--------|---------|--------|------|
| `-webkit-text-size-adjust` | ❌ 已棄用 | ❌ 不支援 | ⚠️ 舊版支援 | ❌ 已棄用 |
| `text-size-adjust` | ✅ 54+ | ✅ 支援 | ✅ 支援 | ✅ 79+ |

---

## 問題 2: `X-XSS-Protection` 標頭

### 問題來源
Spring Security 預設會添加 `X-XSS-Protection: 1; mode=block` 標頭。

這個標頭在現代瀏覽器中已被棄用，因為：
1. 可能引入新的安全漏洞
2. 現代瀏覽器已移除此功能
3. Content-Security-Policy 提供更好的保護

### 解決方案

#### 更新 Spring Security 配置
**文件**: `backend/src/main/java/com/example/demo/config/SecurityConfig.java`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // ...其他配置
        .headers(headers -> headers
            // 禁用過時的 X-XSS-Protection
            .xssProtection(xss -> xss.disable())
            
            // 配置現代安全標頭
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; ...")
            )
            .referrerPolicy(referrer -> referrer
                .policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            )
        );
    
    return http.build();
}
```

### 為什麼要禁用 X-XSS-Protection？

1. **已棄用**: Chrome、Firefox、Edge 都已移除此功能
2. **安全風險**: 在某些情況下可能被利用來創建新的 XSS 漏洞
3. **更好的替代方案**: Content-Security-Policy 提供更強大和靈活的保護

### 現代替代方案

使用 **Content-Security-Policy (CSP)** 標頭：

```
Content-Security-Policy: default-src 'self'; 
                         script-src 'self' 'unsafe-inline' 'unsafe-eval'; 
                         style-src 'self' 'unsafe-inline';
```

CSP 的優勢：
- ✅ 更細粒度的控制
- ✅ 防止多種類型的攻擊（XSS、數據注入等）
- ✅ 所有現代瀏覽器都支援
- ✅ 可以報告違規行為

---

## 完整的安全標頭配置

### Frontend (Nginx)

```nginx
# 現代安全標頭
add_header X-Content-Type-Options "nosniff" always;
add_header X-Frame-Options "SAMEORIGIN" always;
add_header Referrer-Policy "strict-origin-when-cross-origin" always;
add_header Content-Security-Policy "default-src 'self'; ..." always;
add_header Permissions-Policy "geolocation=(), microphone=(), camera=()" always;

# 不添加過時的標頭
# ❌ X-XSS-Protection (已棄用)
```

### Backend (Spring Security)

```java
.headers(headers -> headers
    .xssProtection(xss -> xss.disable())  // 禁用過時標頭
    .contentSecurityPolicy(csp -> csp.policyDirectives("..."))
    .referrerPolicy(referrer -> referrer.policy(...))
    .frameOptions(frame -> frame.sameOrigin())
)
```

---

## 測試驗證

### 1. 檢查 CSS
```bash
# 查看生成的 CSS 文件
curl http://localhost:3000/_nuxt/entry.css | grep "text-size-adjust"

# 應該只看到標準屬性，不應該有 -webkit- 前綴
```

### 2. 檢查 HTTP 標頭
```bash
# 檢查 API 響應標頭
curl -I http://localhost:3000/api/products

# 不應該看到 X-XSS-Protection 標頭
```

### 3. 使用瀏覽器開發者工具
1. 打開 Chrome DevTools
2. 切換到 Network 面板
3. 刷新頁面
4. 檢查響應標頭

---

## 瀏覽器兼容性總結

### 已修復的問題

| 問題 | 狀態 | 解決方案 |
|------|------|----------|
| `-webkit-text-size-adjust` | ✅ 已修復 | 使用標準 `text-size-adjust` |
| `X-XSS-Protection` 標頭 | ✅ 已移除 | 使用 CSP 替代 |

### 現代標頭支援

| 標頭 | Chrome | Firefox | Safari | Edge |
|------|--------|---------|--------|------|
| Content-Security-Policy | ✅ | ✅ | ✅ | ✅ |
| X-Content-Type-Options | ✅ | ✅ | ✅ | ✅ |
| Referrer-Policy | ✅ | ✅ | ✅ | ✅ |
| X-Frame-Options | ✅ | ✅ | ✅ | ✅ |

---

## 相關文件

- `frontend/assets/css/main.css` - 自定義 CSS 覆蓋
- `frontend/nuxt.config.ts` - Nuxt 配置
- `frontend/nginx.conf` - Nginx 安全標頭
- `backend/src/main/java/com/example/demo/config/SecurityConfig.java` - Spring Security 配置

---

## 參考資源

- [MDN: text-size-adjust](https://developer.mozilla.org/en-US/docs/Web/CSS/text-size-adjust)
- [OWASP: Deprecated Headers](https://owasp.org/www-project-secure-headers/)
- [Content Security Policy Reference](https://content-security-policy.com/)
- [Spring Security Headers](https://docs.spring.io/spring-security/reference/features/exploits/headers.html)
