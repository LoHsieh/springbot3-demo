<template>
  <NuxtLayout>
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Order History</h1>

      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading orders...</p>
      </div>

      <div v-else-if="orders.length === 0" class="text-center py-12">
        <p class="text-gray-600 mb-4">No orders yet</p>
        <NuxtLink to="/" class="text-blue-600 hover:text-blue-700">
          Start Shopping
        </NuxtLink>
      </div>

      <div v-else class="space-y-4">
        <div
          v-for="order in orders"
          :key="order.id"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex justify-between items-start mb-4">
            <div>
              <h3 class="text-lg font-semibold text-gray-900">Order #{{ order.id }}</h3>
              <p class="text-sm text-gray-600">{{ formatDate(order.createdAt) }}</p>
            </div>
            <span
              class="px-3 py-1 rounded-full text-sm font-medium"
              :class="{
                'bg-green-100 text-green-800': order.status === 'COMPLETED',
                'bg-yellow-100 text-yellow-800': order.status === 'PENDING',
                'bg-red-100 text-red-800': order.status === 'CANCELLED'
              }"
            >
              {{ order.status }}
            </span>
          </div>

          <div class="border-t pt-4 space-y-2">
            <div class="flex justify-between text-sm">
              <span class="text-gray-600">Subtotal:</span>
              <span>${{ order.totalAmount }}</span>
            </div>
            <div v-if="order.discount > 0" class="flex justify-between text-sm text-green-600">
              <span>Discount ({{ order.couponCode }}):</span>
              <span>-${{ order.discount }}</span>
            </div>
            <div class="flex justify-between text-lg font-bold border-t pt-2">
              <span>Total:</span>
              <span class="text-blue-600">${{ order.finalAmount }}</span>
            </div>
          </div>

          <div class="mt-4">
            <NuxtLink
              :to="`/orders/${order.id}`"
              class="text-blue-600 hover:text-blue-700 text-sm font-medium"
            >
              View Details →
            </NuxtLink>
          </div>
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

const orders = ref([])
const loading = ref(true)

const fetchOrders = async () => {
  try {
    const response = await api.get('/orders')
    orders.value = response.data
  } catch (err) {
    console.error('Failed to load orders:', err)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchOrders()
})
</script>
