<template>
  <NuxtLayout>
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading order...</p>
      </div>

      <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
        {{ error }}
      </div>

      <div v-else-if="order" class="space-y-6">
        <div class="flex items-center justify-between">
          <h1 class="text-3xl font-bold text-gray-900">Order #{{ order.id }}</h1>
          <span
            class="px-4 py-2 rounded-full text-sm font-medium"
            :class="{
              'bg-green-100 text-green-800': order.status === 'COMPLETED',
              'bg-yellow-100 text-yellow-800': order.status === 'PENDING',
              'bg-red-100 text-red-800': order.status === 'CANCELLED'
            }"
          >
            {{ order.status }}
          </span>
        </div>

        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Order Details</h2>
          <p class="text-sm text-gray-600 mb-6">Placed on {{ formatDate(order.createdAt) }}</p>

          <div class="space-y-4">
            <div v-for="item in order.items" :key="item.id" class="flex justify-between border-b pb-4">
              <div>
                <h3 class="font-semibold">{{ item.productName }}</h3>
                <p class="text-sm text-gray-600">Quantity: {{ item.quantity }}</p>
                <p class="text-sm text-gray-600">Price: ${{ item.priceAtPurchase }}</p>
              </div>
              <div class="text-right">
                <p class="font-semibold">${{ (item.priceAtPurchase * item.quantity).toFixed(2) }}</p>
              </div>
            </div>
          </div>

          <div class="border-t mt-6 pt-6 space-y-2">
            <div class="flex justify-between">
              <span class="text-gray-600">Subtotal:</span>
              <span>${{ order.totalAmount }}</span>
            </div>
            <div v-if="order.discount > 0" class="flex justify-between text-green-600">
              <span>Discount ({{ order.couponCode }}):</span>
              <span>-${{ order.discount }}</span>
            </div>
            <div class="flex justify-between text-2xl font-bold border-t pt-4">
              <span>Total:</span>
              <span class="text-blue-600">${{ order.finalAmount }}</span>
            </div>
          </div>
        </div>

        <div class="text-center">
          <NuxtLink to="/orders" class="text-blue-600 hover:text-blue-700">
            ← Back to Orders
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

const route = useRoute()
const api = useApi()

const order = ref(null)
const loading = ref(true)
const error = ref('')

const fetchOrder = async () => {
  try {
    const response = await api.get(`/orders/${route.params.id}`)
    order.value = response.data
  } catch (err: any) {
    error.value = 'Failed to load order details'
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
  fetchOrder()
})
</script>
