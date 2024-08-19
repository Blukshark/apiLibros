import Vue from 'vue';
import Router from 'vue-router';
import AuthForm from '../components/AuthForm.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'AuthForm',
      component: AuthForm,
    },
    // Puedes agregar más rutas aquí
  ],
});



