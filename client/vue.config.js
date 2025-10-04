const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  configureWebpack: {
    resolve: {
      alias: {
        '@': require('path').resolve(__dirname, 'src')
      }
    } 
  },
  transpileDependencies: true,
  devServer: {
    port: 8081
  }
})
