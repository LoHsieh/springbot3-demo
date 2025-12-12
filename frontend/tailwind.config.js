/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./components/**/*.{js,vue,ts}",
        "./layouts/**/*.vue",
        "./pages/**/*.vue",
        "./plugins/**/*.{js,ts}",
        "./app.vue",
    ],
    theme: {
        extend: {},
    },
    plugins: [],
    corePlugins: {
        // 禁用會產生 -webkit-text-size-adjust 的 preflight
        preflight: true,
    },
}
