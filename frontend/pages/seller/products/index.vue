<template>
  <NuxtLayout>
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900">My Products</h1>
        <NuxtLink
          to="/seller/products/create"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Add Product
        </NuxtLink>
      </div>

      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading products...</p>
      </div>

      <div v-else-if="products.length === 0" class="text-center py-12">
        <p class="text-gray-600 mb-4">You haven't added any products yet</p>
        <NuxtLink to="/seller/products/create" class="text-blue-600 hover:text-blue-700">
          Add your first product
        </NuxtLink>
      </div>

      <div v-else class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Image
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Product
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Price
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Stock
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in products" :key="product.id">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="h-12 w-12 rounded-md overflow-hidden bg-gray-100">
                   <img 
                     v-if="product.imageUrl" 
                     :src="product.imageUrl" 
                     :alt="product.name"
                     class="h-full w-full object-cover"
                   />
                   <div v-else class="h-full w-full flex items-center justify-center text-gray-400">
                     <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                       <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                     </svg>
                   </div>
                </div>
              </td>
              <td class="px-6 py-4">
                <div>
                  <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
                  <div class="text-sm text-gray-500">{{ product.description?.substring(0, 50) }}...</div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">${{ product.price }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="product.stock > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                >
                  {{ product.stock }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                <NuxtLink
                  :to="`/seller/products/edit/${product.id}`"
                  class="text-blue-600 hover:text-blue-900"
                >
                  Edit
                </NuxtLink>
                <button
                  @click="confirmDelete(product)"
                  class="text-red-600 hover:text-red-900"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Delete Confirmation Modal -->
      <div v-if="showDeleteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4">
          <h3 class="text-lg font-semibold mb-4">Confirm Delete</h3>
          <p class="text-gray-600 mb-6">
            Are you sure you want to delete "{{ productToDelete?.name }}"? This action cannot be undone.
          </p>
          <div class="flex space-x-4">
            <button
              @click="showDeleteModal = false"
              class="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              @click="deleteProduct"
              :disabled="deleting"
              class="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 disabled:opacity-50"
            >
              {{ deleting ? 'Deleting...' : 'Delete' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({
  middleware: 'seller'
})

const api = useApi()

interface Product {
  id: number
  name: string
  description?: string
  price: number
  stock: number
  imageUrl?: string
}

const products = ref<Product[]>([])
const loading = ref(true)
const showDeleteModal = ref(false)
const productToDelete = ref<Product | null>(null)
const deleting = ref(false)

const fetchProducts = async () => {
  try {
    const response = await api.get('/products/seller/my-products')
    products.value = response.data
  } catch (err) {
    console.error('Failed to load products:', err)
  } finally {
    loading.value = false
  }
}

const confirmDelete = (product: any) => {
  productToDelete.value = product
  showDeleteModal.value = true
}

const deleteProduct = async () => {
  deleting.value = true
  try {
    if (!productToDelete.value) return
    await api.delete(`/products/${productToDelete.value.id}`)
    showDeleteModal.value = false
    await fetchProducts()
  } catch (err: any) {
    alert(err.response?.data?.message || 'Failed to delete product')
  } finally {
    deleting.value = false
  }
}

onMounted(() => {
  fetchProducts()
})
</script>
