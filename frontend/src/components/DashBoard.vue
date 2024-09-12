<template>
  <v-app>
    <NavBar/>
    <v-main>
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
.v-app {
  background-color: #a7cfdb;
}
</style>
