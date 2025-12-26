<template>
  <NuxtLayout>
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Edit Product</h1>

      <div v-if="loading" class="text-center py-12">
        <p class="text-gray-600">Loading product...</p>
      </div>

      <form v-else @submit.prevent="handleSubmit" class="bg-white rounded-lg shadow-md p-6 space-y-6">
        <div v-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {{ error }}
        </div>

        <div>
          <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
            Product Name *
          </label>
          <input
            id="name"
            v-model="form.name"
            type="text"
            autocomplete="off"
            required
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label for="description" class="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            id="description"
            v-model="form.description"
            rows="4"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          ></textarea>
        </div>

        <div>
           <label for="product-image" class="block text-sm font-medium text-gray-700 mb-1">
             Product Image
           </label>
           <div class="mt-1 flex items-center space-x-4">
             <div v-if="previewUrl" class="w-32 h-32 border border-gray-300 rounded-lg overflow-hidden">
               <img :src="previewUrl" alt="Preview" class="w-full h-full object-cover" />
             </div>
             <div class="flex-1">
               <input
                 id="product-image"
                 name="product-image"
                 type="file"
                 accept="image/*"
                 @change="handleFileChange"
                 class="block w-full text-sm text-gray-500
                   file:mr-4 file:py-2 file:px-4
                   file:rounded-full file:border-0
                   file:text-sm file:font-semibold
                   file:bg-blue-50 file:text-blue-700
                   hover:file:bg-blue-100"
               />
               <p v-if="uploading" class="mt-2 text-sm text-blue-600">Uploading...</p>
             </div>
           </div>
        </div>

        <div>
          <label for="price" class="block text-sm font-medium text-gray-700 mb-1">
            Price ($) *
          </label>
          <input
            id="price"
            v-model.number="form.price"
            type="number"
            step="0.01"
            min="0"
            required
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label for="stock" class="block text-sm font-medium text-gray-700 mb-1">
            Stock Quantity *
          </label>
          <input
            id="stock"
            v-model.number="form.stock"
            type="number"
            min="0"
            required
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div class="flex space-x-4">
          <button
            type="button"
            @click="$router.back()"
            class="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="submitting || uploading"
            class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
          >
            {{ submitting ? 'Updating...' : 'Update Product' }}
          </button>
        </div>
      </form>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({
  middleware: 'seller'
})

const route = useRoute()
const api = useApi()
const router = useRouter()

const form = ref({
  name: '',
  description: '',
  price: 0,
  stock: 0,
  imageUrl: ''
})

const previewUrl = ref('')
const loading = ref(true)
const uploading = ref(false)
const submitting = ref(false)
const error = ref('')

const fetchProduct = async () => {
  try {
    const response = await api.get(`/products/${route.params.id}`)
    const product = response.data
    form.value = {
      name: product.name,
      description: product.description || '',
      price: product.price,
      stock: product.stock,
      imageUrl: product.imageUrl || ''
    }
    if (product.imageUrl) {
      previewUrl.value = product.imageUrl
    }
  } catch (err: any) {
    error.value = 'Failed to load product'
  } finally {
    loading.value = false
  }
}

const handleFileChange = async (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files?.length) return

  const file = input.files[0]
  const formData = new FormData()
  formData.append('file', file)

  uploading.value = true
  error.value = ''

  try {
    const { data } = await api.post('/images', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    form.value.imageUrl = data.url
    previewUrl.value = data.url
  } catch (err: any) {
    console.error('Upload error:', err)
    error.value = 'Failed to upload image'
  } finally {
    uploading.value = false
  }
}

const handleSubmit = async () => {
  submitting.value = true
  error.value = ''

  try {
    await api.put(`/products/${route.params.id}`, form.value)
    router.push('/seller/products')
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Failed to update product'
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchProduct()
})
</script>
