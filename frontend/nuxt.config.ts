// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    devtools: { enabled: true },
    modules: [
        '@nuxtjs/tailwindcss',
        '@pinia/nuxt',
        '@pinia-plugin-persistedstate/nuxt'
    ],
    runtimeConfig: {
        public: {
            apiBase: process.env.NUXT_PUBLIC_API_BASE || (process.env.NUXT_PUBLIC_API_HOST ? `https://${process.env.NUXT_PUBLIC_API_HOST}` : 'http://localhost:8080/api')
        }
    },
    ssr: false,

    // 引入自定義 CSS 來覆蓋 Tailwind 的過時屬性
    css: ['~/assets/css/main.css'],

    // SEO and Accessibility optimizations
    app: {
        head: {
            htmlAttrs: {
                lang: 'zh-TW' // 設定語言為繁體中文
            },
            charset: 'utf-8',
            viewport: 'width=device-width, initial-scale=1',
            title: 'E-Commerce Demo - 線上購物平台',
            meta: [
                // 基本 SEO
                { name: 'description', content: '一個功能完整的電子商務示範平台，提供商品瀏覽、購物車、結帳等功能。' },
                { name: 'keywords', content: '電子商務, 線上購物, 購物車, 商品, demo' },
                { name: 'author', content: 'E-Commerce Demo' },

                // Robots
                { name: 'robots', content: 'index, follow' },

                // Open Graph / Facebook
                { property: 'og:type', content: 'website' },
                { property: 'og:title', content: 'E-Commerce Demo - 線上購物平台' },
                { property: 'og:description', content: '一個功能完整的電子商務示範平台，提供商品瀏覽、購物車、結帳等功能。' },
                { property: 'og:site_name', content: 'E-Commerce Demo' },

                // Twitter Card
                { name: 'twitter:card', content: 'summary_large_image' },
                { name: 'twitter:title', content: 'E-Commerce Demo - 線上購物平台' },
                { name: 'twitter:description', content: '一個功能完整的電子商務示範平台' },

                // 無障礙性 - 移除 theme-color（Firefox 不支援）
                { name: 'color-scheme', content: 'light' }
            ],
            link: [
                // Favicon
                { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
            ]
        }
    }
})