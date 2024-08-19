<template>
  <div>
    <form @submit.prevent="submitForm">
      <input v-model="username" type="text" placeholder="Nombre de usuario" />
      <input v-model="password" type="password" placeholder="Contraseña" />
      <button type="submit">Enviar</button>
      <div v-if="error">{{ error }}</div>
    </form>
  </div>
</template>

<script>
import axios from '../plugins/axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
    };
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post('/auth/login', {
          username: this.username,
          password: this.password,
        });
        console.log(response.data); // Maneja la respuesta del backend
        // Almacena el token si se proporciona
        // localStorage.setItem('token', token);
        // Redirige al usuario
        // this.$router.push('/dashboard');
      } catch (error) {
        this.error = 'Error en la solicitud: ' + (error.response ? error.response.data : error.message);
        console.error('Error en la solicitud:', error);
      }
    },
  },
};
</script>