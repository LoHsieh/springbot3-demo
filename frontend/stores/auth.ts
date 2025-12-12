import { defineStore } from 'pinia'
import { jwtDecode } from 'jwt-decode'

interface User {
    userId: number
    username: string
    role: string
}

interface JwtPayload {
    userId: number
    role: string
    sub: string
}

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: null as string | null,
        user: null as User | null
    }),

    getters: {
        isAuthenticated: (state) => !!state.token,
        isBuyer: (state) => state.user?.role === 'BUYER',
        isSeller: (state) => state.user?.role === 'SELLER'
    },

    actions: {
        setAuth(token: string, user: User) {
            this.token = token
            this.user = user
        },

        setToken(token: string) {
            this.token = token
            try {
                const decoded = jwtDecode<JwtPayload>(token)
                this.user = {
                    userId: decoded.userId,
                    username: decoded.sub,
                    role: decoded.role
                }
            } catch (error) {
                console.error('Failed to decode token:', error)
                this.logout()
            }
        },

        logout() {
            this.token = null
            this.user = null
        },

        checkAuth() {
            if (this.token) {
                try {
                    const decoded = jwtDecode<JwtPayload>(this.token)
                    const now = Date.now() / 1000
                    if (decoded.exp && decoded.exp < now) {
                        this.logout()
                        return false
                    }
                    return true
                } catch {
                    this.logout()
                    return false
                }
            }
            return false
        }
    },

    persist: true
})
