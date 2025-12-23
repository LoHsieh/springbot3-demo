<template>
  <NuxtLayout>
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading product...</p>
      </div>

      <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
        {{ error }}
      </div>

      <div v-else-if="product" class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="md:flex">
          <div class="md:w-1/2 h-96 bg-gray-200 flex items-center justify-center overflow-hidden">
            <img 
              v-if="product.imageUrl" 
              :src="product.imageUrl" 
              :alt="product.name"
              class="w-full h-full object-cover"
            />
            <span v-else class="text-gray-400 text-8xl">📦</span>
          </div>

          <div class="md:w-1/2 p-8">
            <h1 class="text-3xl font-bold text-gray-900 mb-4">{{ product.name }}</h1>
            <p class="text-gray-600 mb-6">{{ product.description || 'No description available' }}</p>

            <div class="mb-6">
              <span class="text-4xl font-bold text-blue-600">${{ product.price }}</span>
            </div>

            <div class="mb-6">
              <span class="text-sm text-gray-600">Stock: </span>
              <span class="text-sm font-semibold" :class="product.stock > 0 ? 'text-green-600' : 'text-red-600'">
                {{ product.stock > 0 ? `${product.stock} available` : 'Out of stock' }}
              </span>
            </div>

            <div v-if="authStore.isBuyer && product.stock > 0" class="space-y-4">
              <div class="flex items-center space-x-4">
                <label class="text-sm font-medium text-gray-700">Quantity:</label>
                <input
                  v-model.number="quantity"
                  type="number"
                  min="1"
                  :max="product.stock"
                  class="w-20 px-3 py-2 border border-gray-300 rounded-md"
                />
              </div>

              <button
                @click="addToCart"
                :disabled="addingToCart"
                class="w-full py-3 px-6 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
              >
                {{ addingToCart ? 'Adding...' : 'Add to Cart' }}
              </button>

              <div v-if="successMessage" class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                {{ successMessage }}
              </div>
            </div>

            <div v-else-if="!authStore.isAuthenticated" class="text-center">
              <NuxtLink to="/login" class="text-blue-600 hover:text-blue-700">
                Login to purchase
              </NuxtLink>
            </div>
          </div>
        </div>
      </div>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({
  layout: 'default'
})

import type { Product } from '~/composables/useTypes'

const route = useRoute()
const api = useApi()
const authStore = useAuthStore()

const product = ref<Product | null>(null)
const loading = ref(true)
const error = ref('')
const quantity = ref(1)
const addingToCart = ref(false)
const successMessage = ref('')

const fetchProduct = async () => {
  try {
    const response = await api.get(`/products/${route.params.id}`)
    product.value = response.data
  } catch (err: any) {
    error.value = 'Failed to load product'
  } finally {
    loading.value = false
  }
}

const addToCart = async () => {
  addingToCart.value = true
  successMessage.value = ''

  try {
    await api.post('/cart', {
      productId: product.value?.id,
      quantity: quantity.value
    })
    successMessage.value = 'Added to cart successfully!'
    setTimeout(() => {
      successMessage.value = ''
    }, 3000)
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Failed to add to cart'
  } finally {
    addingToCart.value = false
  }
}

onMounted(() => {
  fetchProduct()
})
</script>
