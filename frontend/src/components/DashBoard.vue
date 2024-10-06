<template>
  <v-app>
    <NavBar/>
    <v-main class="custom-bg">
      <v-container>
        <v-toolbar-title>Bienvenido a la pagina principal de la biblioteca, {{ username }} </v-toolbar-title>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import NavBar from './NavBar.vue';
import auth from '../utils/auth';
export default {
  components: {
    NavBar
  },
  data(){
    return{
      username: auth.getAuthenticatedUser(),
    }
  }
};

</script>

<style scoped>
.custom-bg {
  background-color: #a5aac9;
}
</style>
