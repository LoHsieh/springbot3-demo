<template>
  <div class="min-h-screen bg-gray-50">
    <nav class="bg-white shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <NuxtLink to="/" class="text-xl font-bold text-gray-900">
              E-commerce MVP
            </NuxtLink>
          </div>

          <div class="flex items-center space-x-4">
            <template v-if="authStore.isAuthenticated">
              <template v-if="authStore.isBuyer">
                <NuxtLink to="/" class="text-gray-700 hover:text-gray-900">Products</NuxtLink>
                <NuxtLink to="/cart" class="text-gray-700 hover:text-gray-900">Cart</NuxtLink>
                <NuxtLink to="/orders" class="text-gray-700 hover:text-gray-900">Orders</NuxtLink>
              </template>

              <template v-if="authStore.isSeller">
                <NuxtLink to="/seller/products" class="text-gray-700 hover:text-gray-900">My Products</NuxtLink>
                <NuxtLink to="/seller/products/create" class="text-gray-700 hover:text-gray-900">Add Product</NuxtLink>
              </template>

              <span class="text-sm text-gray-600">{{ authStore.user?.username }}</span>
              <button @click="handleLogout" class="text-red-600 hover:text-red-700">Logout</button>
            </template>

            <template v-else>
              <NuxtLink to="/login" class="text-gray-700 hover:text-gray-900">Login</NuxtLink>
              <NuxtLink to="/register" class="text-blue-600 hover:text-blue-700 font-medium">Register</NuxtLink>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <main>
      <slot />
    </main>
  </div>
</template>

<script setup lang="ts">
const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>
