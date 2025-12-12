<template>
  <NuxtLayout>
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Checkout</h1>

      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading...</p>
      </div>

      <div v-else-if="cartItems.length === 0" class="text-center py-12">
        <p class="text-gray-600 mb-4">Your cart is empty</p>
        <NuxtLink to="/" class="text-blue-600 hover:text-blue-700">
          Continue Shopping
        </NuxtLink>
      </div>

      <div v-else class="space-y-6">
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Order Summary</h2>
          
          <div class="space-y-3 mb-6">
            <div v-for="item in cartItems" :key="item.id" class="flex justify-between text-sm">
              <span>{{ item.product?.name }} × {{ item.quantity }}</span>
              <span>${{ (item.product?.price * item.quantity).toFixed(2) }}</span>
            </div>
          </div>

          <div class="border-t pt-4 space-y-2">
            <div class="flex justify-between text-lg">
              <span>Subtotal:</span>
              <span>${{ subtotal.toFixed(2) }}</span>
            </div>

            <div v-if="discount > 0" class="flex justify-between text-lg text-green-600">
              <span>Discount:</span>
              <span>-${{ discount.toFixed(2) }}</span>
            </div>

            <div class="flex justify-between text-2xl font-bold text-blue-600 pt-2 border-t">
              <span>Total:</span>
              <span>${{ finalAmount.toFixed(2) }}</span>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Coupon Code</h2>
          
          <div class="flex space-x-2">
            <input
              v-model="couponCode"
              type="text"
              placeholder="Enter coupon code"
              class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              @click="applyCoupon"
              :disabled="!couponCode || applyingCoupon"
              class="px-6 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 disabled:opacity-50"
            >
              {{ applyingCoupon ? 'Applying...' : 'Apply' }}
            </button>
          </div>

          <div v-if="couponError" class="mt-2 text-sm text-red-600">
            {{ couponError }}
          </div>
          <div v-if="couponSuccess" class="mt-2 text-sm text-green-600">
            {{ couponSuccess }}
          </div>
        </div>

        <div v-if="checkoutError" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {{ checkoutError }}
        </div>

        <button
          @click="handleCheckout"
          :disabled="processingCheckout"
          class="w-full py-3 px-6 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
        >
          {{ processingCheckout ? 'Processing...' : 'Place Order' }}
        </button>
      </div>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({
  middleware: 'buyer'
})

const api = useApi()
const router = useRouter()

const cartItems = ref([])
const loading = ref(true)
const couponCode = ref('')
const discount = ref(0)
const applyingCoupon = ref(false)
const couponError = ref('')
const couponSuccess = ref('')
const processingCheckout = ref(false)
const checkoutError = ref('')

const subtotal = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + (item.product?.price || 0) * item.quantity
  }, 0)
})

const finalAmount = computed(() => {
  return Math.max(0, subtotal.value - discount.value)
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

const applyCoupon = async () => {
  applyingCoupon.value = true
  couponError.value = ''
  couponSuccess.value = ''

  try {
    // We'll validate the coupon during checkout
    // For now, just show a message
    couponSuccess.value = 'Coupon will be applied at checkout'
  } catch (err: any) {
    couponError.value = err.response?.data?.message || 'Invalid coupon code'
  } finally {
    applyingCoupon.value = false
  }
}

const handleCheckout = async () => {
  processingCheckout.value = true
  checkoutError.value = ''

  try {
    const response = await api.post('/orders/checkout', {
      couponCode: couponCode.value || null
    })
    
    // Redirect to order confirmation
    router.push(`/orders/${response.data.id}`)
  } catch (err: any) {
    checkoutError.value = err.response?.data?.message || 'Checkout failed. Please try again.'
  } finally {
    processingCheckout.value = false
  }
}

onMounted(() => {
  fetchCart()
})
</script>
