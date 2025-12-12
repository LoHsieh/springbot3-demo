# E-commerce MVP - 開發環境

完整的電商 MVP 專案，使用 Spring Boot 3、Nuxt 3 和 PostgreSQL 構建。

## 🚀 快速開始

### 前置需求
- Docker Desktop
- Git (可選)

### 啟動專案

```bash
# 1. 啟動所有服務
docker-compose up -d

# 2. 查看日誌
docker-compose logs -f

# 3. 訪問應用
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# pgAdmin: http://localhost:5050
```

## ✨ 開發環境特性

### 🔥 熱重載 (Hot Reload)

#### Backend (Spring Boot)
- ✅ 修改 `backend/src` 中的 Java 代碼會自動重啟（2-5秒）
- ✅ 使用 Spring Boot DevTools
- ✅ Maven 依賴緩存持久化

#### Frontend (Nuxt 3)
- ✅ 修改 Vue/TypeScript 代碼即時更新（<1秒）
- ✅ 熱模塊替換 (HMR)
- ✅ 保持組件狀態

### 🐛 遠程調試

#### Backend
- 調試端口：`5005`
- IntelliJ IDEA: 創建 Remote JVM Debug 配置
  - Host: `localhost`
  - Port: `5005`

#### Frontend
- HMR 端口：`24678`
- 使用瀏覽器開發工具或 VS Code 調試

## 📦 技術堆棧

### Backend
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL
- JPA/Hibernate
- SpringDoc OpenAPI (Swagger)

### Frontend
- Nuxt 3
- Vue 3
- Pinia (狀態管理)
- Tailwind CSS
- Axios

## 🎯 核心功能

- ✅ JWT 身份驗證（買家/賣家角色）
- ✅ 商品管理 (CRUD)
- ✅ 購物車功能
- ✅ 優惠券結帳（固定金額折扣）
- ✅ 訂單管理
- ✅ API 文檔 (Swagger)

## 🧪 測試帳號

### 買家帳號
```
Username: buyer
Password: password
```

### 賣家帳號
```
Username: seller
Password: password
```

### 優惠券代碼
- `SAVE10` - $10 折扣
- `SAVE50` - $50 折扣

## 📝 開發工作流程

### Backend 開發

1. **修改代碼**：編輯 `backend/src/main/java` 中的文件
2. **自動重啟**：Spring Boot DevTools 會檢測變更並重啟
3. **測試 API**：訪問 http://localhost:8080/swagger-ui.html

### Frontend 開發

1. **修改代碼**：編輯 `frontend/pages`、`frontend/components` 等
2. **即時更新**：瀏覽器自動刷新
3. **測試應用**：訪問 http://localhost:3000

## 🛠️ 常用命令

```bash
# 啟動所有服務
docker-compose up -d

# 查看日誌
docker-compose logs -f backend
docker-compose logs -f frontend

# 重啟服務
docker-compose restart backend
docker-compose restart frontend

# 停止所有服務
docker-compose down

# 重新構建並啟動
docker-compose up -d --build

# 清除所有數據（包括數據庫）
docker-compose down -v
```

## 📂 專案結構

```
demo/
├── backend/                 # Spring Boot 後端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/demo/
│   │       │   ├── component/      # 數據初始化
│   │       │   ├── config/         # 配置類
│   │       │   ├── controller/     # REST 控制器
│   │       │   ├── dto/            # 數據傳輸對象
│   │       │   ├── entity/         # JPA 實體
│   │       │   ├── exception/      # 異常處理
│   │       │   ├── repository/     # 數據訪問層
│   │       │   ├── security/       # JWT 安全
│   │       │   └── service/        # 業務邏輯
│   │       └── resources/
│   │           ├── application.yml
│   │           └── application-docker.yml
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # Nuxt 3 前端
│   ├── components/
│   ├── composables/         # useApi
│   ├── layouts/             # 佈局
│   ├── middleware/          # 路由中間件
│   ├── pages/               # 頁面/路由
│   ├── stores/              # Pinia 狀態
│   ├── Dockerfile
│   ├── nuxt.config.ts
│   └── package.json
├── docker-compose.yml       # Docker 配置
├── .env                     # 環境變數
└── README.md
```

## 🔍 數據庫管理

### 使用 pgAdmin

1. 訪問 http://localhost:5050
2. 登入：
   - Email: `admin@admin.com`
   - Password: `admin`
3. 添加服務器：
   - Host: `postgres`
   - Port: `5432`
   - Database: `ecommerce`
   - Username: `postgres`
   - Password: `postgres`

## 🐛 故障排除

### Backend 不自動重啟

```bash
# 檢查日誌
docker-compose logs backend

# 重啟服務
docker-compose restart backend
```

### Frontend HMR 不工作

```bash
# 清除緩存
docker-compose exec frontend rm -rf .nuxt
docker-compose restart frontend
```

### 端口衝突

修改 `docker-compose.yml` 中的端口映射：
```yaml
ports:
  - "8081:8080"  # 改為其他端口
```

## 📚 相關文檔

- [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) - API 接口文檔
- [DEPLOYMENT.md](./DEPLOYMENT.md) - 部署指南

## 🎓 使用流程

### 買家流程
1. 註冊/登入為買家
2. 瀏覽商品
3. 加入購物車
4. 結帳（可使用優惠券）
5. 查看訂單

### 賣家流程
1. 註冊/登入為賣家
2. 管理商品（新增/編輯/刪除）

## 📄 授權

MIT License
