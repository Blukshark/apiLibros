import Vue from 'vue';
import App from './App.vue';
import router from './router'; // Importa el enrutador aquí

Vue.config.productionTip = false;

new Vue({
  router, // Usa el enrutador aquí
  render: h => h(App),
}).$mount('#app');

