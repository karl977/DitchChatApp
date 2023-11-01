import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
// https://ruslan.rocks/posts/vite-proxy-how-to-setup
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server:{
    headers:{
      "X-Frame-Options": "ALLOW"
    },
    proxy:{
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        autoRewrite: true
      },
      '/websocket': {
        target: 'ws://localhost:8080',
        ws: true,
      },
    },
    port: 80
  }
})
