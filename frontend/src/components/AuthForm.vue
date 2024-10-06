<template>
  <v-app>
    <v-main class="custom-bg">
      <v-container>
        <v-card class="pa-5" min-height="425" min-width="480">
          <v-card-title>
            <span class="text-h5">Iniciar Sesión</span>
          </v-card-title>

          <v-card-text>
            <!-- .prevent evita el comportamiento por defecto del form, evitando recargar la página -->
            <v-form @submit.prevent="submitForm">
              <!-- input nombre de usuario -->
              <v-text-field v-model="username" label="Nombre de usuario" prepend-icon="mdi-account" outlined
                required></v-text-field>

              <!-- input contraseña -->
              <v-text-field v-model="password" label="Contraseña" prepend-icon="mdi-lock" type="password" outlined
                required></v-text-field>

              <!-- Botón para enviar el formulario -->
              <v-btn type="submit" color="primary" block>
                Enviar
              </v-btn>

              <!-- Alerta de error, se ejecuta si la variable "error" es true o NO es null-->
              <v-alert v-if="error" type="error" outlined class="mt-4">
                {{ error }}
              </v-alert>
            </v-form>
          </v-card-text>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import axios from '../plugins/axios'; // Import de Axios para solicitudes HTTP
import auth from '../utils/auth'; // import de la funcion de autenticación

export default {
  data() {
    return {
      username: '',  // Variable para el nombre de usuario
      password: '',  // Variable para la contraseña
      error: '',     // Variable para almacenar errores
    };
  },
  created() { // Hook que se ejecuta cuando el componente es creado
    auth.logout(); // Al cargar el componente de login, cerramos la sesión previa para evitar accesos sin autenticación
  },
  methods: {
    // Método asíncrono para manejar el envío del formulario de inicio de sesión
    async submitForm() {
      try {
        // Realizas la petición al backend para autenticar al usuario
        const response = await axios.post('/auth/login', {
          username: this.username,
          password: this.password,
        });

        if (response.status === 200) {
          const response2 = await axios.get(`/clientes/nombre/${this.username}`);

          const user = response2.data; // recibo el usuario
          console.log(user);
          // Guardar username en localStorage
          auth.setUsername(user.username);

          // Guardar la ID del cliente en localStorage
          auth.setClientId(user.id_cliente); // Guardar el ID del cliente que recibes

          // Marcar como autenticado
          auth.login();

          // Redirigir al dashboard
          this.$router.push('/dashboard');
        }
      } catch (error) {
        // Mostrar el mensaje de error
        this.error = 'Error en la solicitud: ' + (error.response ? error.response.data : error.message);
      }
    }
    ,
  },
};
</script>

<style scoped>
.custom-bg {
  background-color: #a5aac9;
}

.v-container {
  min-height: 90vh;
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
  margin-bottom: 16px;
  margin-top: 16px;
}

.v-alert {
  margin-top: 16px;
}

.v-card-title {
  align-self: center;
}
</style>
