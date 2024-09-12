<template>
  <v-app-bar app color="primary" dark>
    <v-btn text to="/DashBoard">Inicio</v-btn>
    <v-btn text to="/libros">Libros</v-btn>
    <v-btn text to="/autores">Autores</v-btn>
    <v-btn text to="/prestamos">Préstamos</v-btn>

    <v-spacer></v-spacer>

    <v-btn @click="miPerfil" prepend-icon="mdi-account" stacked variant="tonal">
      Perfil
    </v-btn>

    <v-btn text @click="cerrarSesion" class="logout-btn">
      <span class="">Cerrar sesión</span>
    </v-btn>

  </v-app-bar>
</template>

<script>
import auth from '../utils/auth'; // importo las funciones de auth.js

export default {
  methods: {
    cerrarSesion() {
      auth.logout(); //cierre de sesión
      this.$router.push('/'); // Redirigir al login después de cerrar sesión
    },
    miPerfil(){
      this.$router.push('/perfil'); // Redirigir a una pestaña de gestion de cuenta de cliente.
    }
  },
};
</script>

<style scoped>

</style>
