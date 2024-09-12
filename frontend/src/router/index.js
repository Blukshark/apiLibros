import { createRouter, createWebHistory } from 'vue-router';
import AuthForm from '../components/AuthForm.vue';
import DashBoard from '../components/DashBoard.vue';
import GestionLibros from '../components/Libros.vue';
import GestionAutores from '../components/Autores.vue';
import GestionPrestamos from '../components/Prestamos.vue';

// Importa todas las funciones de autenticación
import auth from '../utils/auth';

//meta requiresAuth true me indica que esa ruta requiere autenticacion para ser accedida
const routes = [
  { path: '/',name: 'AuthForm',component: AuthForm, meta: { requiresAuth: false }, },//aca puedo acceder sin autenticacion
  { path: '/dashboard',name: 'Dashboard',component: DashBoard, meta: { requiresAuth: true }, },
  { path: '/libros', component: GestionLibros, meta: { requiresAuth: true }, },
  { path: '/autores', component: GestionAutores, meta: { requiresAuth: true }, },
  { path: '/prestamos', component: GestionPrestamos, meta: { requiresAuth: true }, },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Configurar la guardia global
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!auth.isAuthenticated()) {
      // Si no está autenticado, redirigir al formulario de inicio de sesión
      next({ name: 'AuthForm' });
    } else {
      // Si está autenticado, permitir acceso
      next();
    }
  } else {
    // Si la ruta no requiere autenticación, permitir acceso
    next();
  }
});

export default router;
