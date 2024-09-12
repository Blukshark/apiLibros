<template>
  <v-container class="d-flex justify-center align-center" fill-height>
    <v-card class="pa-5" max-width="400" min-width="300">
      <v-card-title>
        <span class="text-h5">Iniciar Sesión</span>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="submitForm">
          <v-text-field
            v-model="username"
            label="Nombre de usuario"
            prepend-icon="mdi-account"
            outlined
            required
          ></v-text-field>

          <v-text-field
            v-model="password"
            label="Contraseña"
            prepend-icon="mdi-lock"
            type="password"
            outlined
            required
          ></v-text-field>

          <v-btn type="submit" color="primary" block>
            Enviar
          </v-btn>

          <v-alert v-if="error" type="error" outlined class="mt-4">
            {{ error }}
          </v-alert>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from '../plugins/axios';
import auth from '../utils/auth';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
    };
  },
  created(){//se ejecuta cuando se crea el componente
    auth.logout();//esto es para evitar que al volver al authForm, se pueda saltar la validacion
                  // y volver al Dashboard sin logearse
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post('/auth/login', {
          username: this.username,
          password: this.password,
        });

        if (response.status === 200) {
          auth.login();
          this.$router.push('/dashboard');
        }
      } catch (error) {
        this.error = 'Error en la solicitud: ' + (error.response ? error.response.data : error.message);
      }
    },
  },
};
</script>

<style scoped>
.v-container {
  background-color: #f5f5f5;
  min-height: 90vh; /* Asegura que el contenedor tenga al menos el alto de la vista */
  display: flex;
  justify-content: center;
  align-items: center;
}

.v-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.v-text-field,
.v-btn {
  margin-bottom: 16px; /* Espacio entre los campos y el botón */
  margin-top: 16px; /* Espacio adicional para el botón */
}

.v-alert {
  margin-top: 16px;
}
</style>
