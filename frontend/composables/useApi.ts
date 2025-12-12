import axios from 'axios'
import { useAuthStore } from '~/stores/auth'

export const useApi = () => {
    const config = useRuntimeConfig()
    const authStore = useAuthStore()

    const api = axios.create({
        baseURL: config.public.apiBase as string,
        headers: {
            'Content-Type': 'application/json'
        }
    })

    // Request interceptor to add JWT token
    api.interceptors.request.use(
        (config) => {
            if (authStore.token) {
                config.headers.Authorization = `Bearer ${authStore.token}`
            }
            return config
        },
        (error) => {
            return Promise.reject(error)
        }
    )

    // Response interceptor for error handling
    api.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response?.status === 401) {
                authStore.logout()
                navigateTo('/login')
            }
            return Promise.reject(error)
        }
    )

    return api
}
