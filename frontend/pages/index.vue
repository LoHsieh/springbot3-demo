<template>
  <NuxtLayout>
    <!-- 主要內容區域 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8" role="main">
      <!-- 頁面標題 -->
      <header class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">商品列表</h1>
        <p class="text-gray-600 mt-2">瀏覽我們精選的商品</p>
      </header>

      <!-- 載入中狀態 -->
      <div 
        v-if="loading" 
        class="text-center py-12"
        role="status"
        aria-live="polite"
        aria-label="正在載入商品"
      >
        <p class="text-gray-600">載入商品中...</p>
      </div>

      <!-- 錯誤狀態 -->
      <div 
        v-else-if="error" 
        class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded"
        role="alert"
        aria-live="assertive"
      >
        <strong class="font-bold">錯誤：</strong>
        <span>{{ error }}</span>
      </div>

      <!-- 無商品狀態 -->
      <div 
        v-else-if="products.length === 0" 
        class="text-center py-12"
        role="status"
      >
        <p class="text-gray-600">目前沒有可用的商品</p>
      </div>

      <!-- 商品列表 -->
      <section 
        v-else 
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6"
        aria-label="商品列表"
      >
        <article
          v-for="product in products"
          :key="product.id"
          class="bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow overflow-hidden"
          :aria-labelledby="`product-title-${product.id}`"
        >
          <!-- 商品圖片 -->
          <div 
            class="h-48 bg-gray-200 flex items-center justify-center overflow-hidden"
            role="img"
            :aria-label="`${product.name} 的商品圖片`"
          >
            <img 
              v-if="product.imageUrl" 
              :src="product.imageUrl" 
              :alt="product.name"
              class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
            <span v-else class="text-gray-400 text-4xl" aria-hidden="true">📦</span>
          </div>
          
          <!-- 商品資訊 -->
          <div class="p-4">
            <h2 
              :id="`product-title-${product.id}`"
              class="text-lg font-semibold text-gray-900 mb-2"
            >
              {{ product.name }}
            </h2>
            
            <p class="text-gray-600 text-sm mb-3 line-clamp-2">
              {{ product.description || '暫無描述' }}
            </p>
            
            <!-- 價格和庫存 -->
            <div class="flex items-center justify-between mb-3">
              <span 
                class="text-2xl font-bold text-blue-600"
                aria-label="`價格 ${product.price} 元`"
              >
                ${{ product.price }}
              </span>
              <span 
                class="text-sm text-gray-500"
                :aria-label="`庫存 ${product.stock} 件`"
              >
                庫存: {{ product.stock }}
              </span>
            </div>

            <!-- 查看詳情按鈕 -->
            <NuxtLink
              :to="`/products/${product.id}`"
              class="block w-full text-center py-2 px-4 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              :aria-label="`查看 ${product.name} 的詳細資訊`"
            >
              查看詳情
            </NuxtLink>
          </div>
        </article>
      </section>
    </main>
  </NuxtLayout>
</template>

<script setup lang="ts">
import type { Product } from '~/composables/useTypes'

// SEO Meta 標籤
useHead({
  title: '商品列表 - E-Commerce Demo',
  meta: [
    { name: 'description', content: '瀏覽我們精選的商品，包含各種類別的優質產品。' },
    { name: 'keywords', content: '商品, 購物, 線上商店, 電子商務' },
    // Open Graph
    { property: 'og:title', content: '商品列表 - E-Commerce Demo' },
    { property: 'og:description', content: '瀏覽我們精選的商品，包含各種類別的優質產品。' },
    { property: 'og:type', content: 'website' }
  ]
})

const api = useApi()

const products = ref<Product[]>([])
const loading = ref(true)
const error = ref('')

const fetchProducts = async () => {
  try {
    const response = await api.get('/products')
    products.value = response.data
  } catch (err: any) {
    error.value = '無法載入商品列表'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchProducts()
})
</script>
