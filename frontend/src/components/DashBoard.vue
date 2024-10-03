<template>
  <v-app>
    <NavBar/>
    <v-main class="custom-bg">
      <v-container>
        <h1>Bienvenido al Dashboard</h1>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import NavBar from './NavBar.vue';

export default {
  components: {
    NavBar
  },
};
</script>

<style scoped>
.custom-bg {
  background-color: #a5aac9;
}
</style>
