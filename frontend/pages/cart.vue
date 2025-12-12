<template>
  <NuxtLayout>
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Shopping Cart</h1>

      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading cart...</p>
      </div>

      <div v-else-if="cartItems.length === 0" class="text-center py-12">
        <p class="text-gray-600 mb-4">Your cart is empty</p>
        <NuxtLink to="/" class="text-blue-600 hover:text-blue-700">
          Continue Shopping
        </NuxtLink>
      </div>

      <div v-else>
        <div class="bg-white rounded-lg shadow-md overflow-hidden mb-6">
          <div v-for="item in cartItems" :key="item.id" class="border-b last:border-b-0 p-6">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4 flex-1">
                <div class="w-20 h-20 bg-gray-200 rounded flex items-center justify-center">
                  <span class="text-gray-400 text-2xl">📦</span>
                </div>
                
                <div class="flex-1">
                  <h3 class="text-lg font-semibold text-gray-900">{{ item.product?.name }}</h3>
                  <p class="text-gray-600">${{ item.product?.price }}</p>
                </div>
              </div>

              <div class="flex items-center space-x-6">
                <div class="flex items-center space-x-2">
                  <button
                    @click="updateQuantity(item.id, item.quantity - 1)"
                    :disabled="item.quantity <= 1"
                    class="w-8 h-8 rounded border border-gray-300 hover:bg-gray-100 disabled:opacity-50"
                  >
                    -
                  </button>
                  <span class="w-12 text-center">{{ item.quantity }}</span>
                  <button
                    @click="updateQuantity(item.id, item.quantity + 1)"
                    :disabled="item.quantity >= item.product?.stock"
                    class="w-8 h-8 rounded border border-gray-300 hover:bg-gray-100 disabled:opacity-50"
                  >
                    +
                  </button>
                </div>

                <div class="w-24 text-right font-semibold">
                  ${{ (item.product?.price * item.quantity).toFixed(2) }}
                </div>

                <button
                  @click="removeItem(item.id)"
                  class="text-red-600 hover:text-red-700"
                >
                  Remove
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="flex justify-between items-center mb-6">
            <span class="text-xl font-semibold">Total:</span>
            <span class="text-3xl font-bold text-blue-600">${{ total.toFixed(2) }}</span>
          </div>

          <NuxtLink
            to="/checkout"
            class="block w-full text-center py-3 px-6 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors"
          >
            Proceed to Checkout
          </NuxtLink>
        </div>
      </div>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({
  middleware: 'buyer'
})

const api = useApi()

const cartItems = ref([])
const loading = ref(true)

const total = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + (item.product?.price || 0) * item.quantity
  }, 0)
})

const fetchCart = async () => {
  try {
    const response = await api.get('/cart')
    cartItems.value = response.data
  } catch (err) {
    console.error('Failed to load cart:', err)
  } finally {
    loading.value = false
  }
}

const updateQuantity = async (itemId: number, newQuantity: number) => {
  try {
    await api.put(`/cart/${itemId}?quantity=${newQuantity}`)
    await fetchCart()
  } catch (err: any) {
    alert(err.response?.data?.message || 'Failed to update quantity')
  }
}

const removeItem = async (itemId: number) => {
  try {
    await api.delete(`/cart/${itemId}`)
    await fetchCart()
  } catch (err) {
    alert('Failed to remove item')
  }
}

onMounted(() => {
  fetchCart()
})
</script>
