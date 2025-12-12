export default defineNuxtRouteMiddleware((to, from) => {
    const authStore = useAuthStore()

    if (!authStore.checkAuth()) {
        return navigateTo('/login')
    }
})
