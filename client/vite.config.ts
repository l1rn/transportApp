import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import eslintPlugin from 'vite-plugin-eslint'

export default defineConfig({
  plugins: [
    vue(),
    eslintPlugin({
      include: ['src/**/*.js', 'src/**/*.jsx', 'src/**/*.ts', 'src/**/*.tsx'],
      exclude: ['node_modules/**', 'dist/**'],
      cache: false, 
      fix: false,
      emitError: true,
      emitWarning: true,
      failOnError: false,
      failOnWarning: false,
    })
  ],

  resolve: {
    alias: {
      '@': '/src'
    }
  },  
  server: {
    port: 8081,
    host: true,
    hmr: {
      overlay: false
    }
  }
})