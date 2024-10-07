const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081, //para que se ejecute siempre en el puerto 8081
    proxy: {
      '/api': {
        target: 'http://localhost:8080',//redirige las solicitudes /api al backend
        changeOrigin: true,
      },
    },
  },
});