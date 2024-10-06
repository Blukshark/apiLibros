<template>
  <v-app class="custom-bg">
    <v-container>
      <navBar />
    </v-container>

    <v-container>
      <h2>Perfil de: {{ loggedUser.nombre }} {{ loggedUser.apellido }} - username: {{ cliente }}</h2>
      <!-- Edición del perfil del usuario logueado -->
      <v-row v-if="!isAdmin">
        <v-col cols="12" md="6">

          <!-- Botones para modificar el perfil -->
          <v-btn @click="openEditProfileDialog()" color="primary">Modificar mi perfil</v-btn>

          <!-- Formulario de edición de perfil propio en un cuadro de diálogo -->
          <v-dialog v-model="editProfile" max-width="500px">
            <v-card>
              <v-card-title>Modificar Perfil</v-card-title>
              <v-card-text>
                <v-form>
                  <v-text-field v-model="editedUser.nombre" label="Nombre"></v-text-field>
                  <v-text-field v-model="editedUser.apellido" label="Apellido"></v-text-field>
                  <v-text-field v-model="editedUser.username" label="Username"></v-text-field>
                  <v-text-field v-model="editedUser.password" label="Contraseña" type="password"></v-text-field>
                </v-form>
              </v-card-text>

              <v-card-actions>
                <v-btn color="primary" @click="updateUserProfile(editedUser)">Guardar cambios</v-btn>
                <v-btn @click="cancelEditProfile">Cancelar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
      </v-row>

      <!-- Solo visible para admin: Listado de usuarios en tabla -->
      <v-row v-if="isAdmin">
        <v-col cols="12" md="6">
          <h2>Listado de Usuarios</h2>
          <v-simple-table class="simple-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>Username</th>
                <th>Password</th>
                <th>Rol</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cliente in clientes" :key="cliente.id_cliente">
                <td>{{ cliente.id_cliente }}</td>
                <td>{{ cliente.nombre }}</td>
                <td>{{ cliente.apellido }}</td>
                <td>{{ cliente.username }}</td>
                <td>{{ cliente.password }}</td>
                <td>{{ cliente.rol }}</td>
                <td>
                  <v-btn color="primary" small @click="editSelectedUser(cliente)">Modificar</v-btn>
                  <v-btn color="red" small @click="deleteUser(cliente.id_cliente)">Eliminar</v-btn>
                </td>
              </tr>
            </tbody>
          </v-simple-table>
        </v-col>
      </v-row>

      <!-- Formulario para modificar cliente seleccionado (solo admin) -->
      <v-row v-if="isAdmin && verFormMod">
        <v-col cols="12" md="6">
          <h2>Modificar Usuario</h2>
          <v-form>
            <v-text-field v-model="selectedUser.nombre" label="Nombre"></v-text-field>
            <v-text-field v-model="selectedUser.apellido" label="Apellido"></v-text-field>
            <v-text-field v-model="selectedUser.username" label="Username"></v-text-field>
            <v-text-field v-model="selectedUser.password" label="Contraseña" type="password"></v-text-field>
            <v-select v-model="selectedUser.rol" :items="['usuario', 'admin']" label="Elegir Rol" outlined required
              item-value="rol"></v-select>

            <v-btn @click="updateClient(selectedUser)" color="primary">Actualizar Usuario</v-btn>
            <v-btn @click="clearSelectedUser" color="secondary">Cancelar</v-btn>
          </v-form>
        </v-col>
      </v-row>

      <!-- Solo visible para admin: Creación de nuevos usuarios -->
      <v-row v-if="isAdmin">
        <v-col cols="12" md="6">
          <h2>Crear Nuevo Usuario</h2>
          <v-form>
            <v-text-field v-model="nuevoCliente.nombre" label="Nombre"></v-text-field>
            <v-text-field v-model="nuevoCliente.apellido" label="Apellido"></v-text-field>
            <v-text-field v-model="nuevoCliente.username" label="Username"></v-text-field>
            <v-text-field v-model="nuevoCliente.password" label="Contraseña" type="password"></v-text-field>
            <v-select v-model="nuevoCliente.rol" :items="['usuario', 'admin']" label="Elegir Rol" outlined required
              item-value="rol"></v-select>
            <v-btn @click="createCliente" color="primary">Crear Usuario</v-btn>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import axios from '../plugins/axios';
import navBar from './NavBar.vue';
import auth from '../utils/auth.js'; // Autenticación del usuario logueado
import '../assets/styles.css';

export default {
  components: {
    navBar,
  },
  data() {
    return {
      cliente: auth.getAuthenticatedUser(),
      loggedUser: {
        nombre: '',
        apellido: '',
        username: '',
        password: '',
        rol: '',
      },
      clientes: [], // Lista de todos los nombres de usuario
      nuevoCliente: {
        nombre: '',
        apellido: '',
        username: '',
        password: '',
        rol: '',
      },
      selectedUser: { // El usuario seleccionado para editar
        nombre: '',
        apellido: '',
        username: '',
        password: '',
        rol: '',
      },
      editedUser: null, //Este cliente es temporal, usado para copiar y ver en el form
      isAdmin: false, // Bandera para saber si el usuario es admin
      editProfile: false,
      confirmDelete: false,
      verFormMod: false,
    };
  },
  mounted() {
    this.getLoggedUser();
  },
  methods: {
    // Crear una copia de `loggedUser` al abrir el formulario
    openEditProfileDialog() {
      this.editedUser = { ...this.loggedUser }; // hago una copia del cliente logeado
      this.editProfile = true; // Mostrar el cuadro de diálogo
    },
    // Cancelar la edición y cerrar el cuadro de diálogo
    cancelEditProfile() {
      this.editProfile = false; // Cierra el diálogo
    },
    editarMiPerfil() {
      this.editProfile = true;  //Si no modifico el form de perfil despues puedo mover esta linea
    },
    //obtener el usuario que inicio sesion
    getLoggedUser() {
      const idCliente = auth.getClientId();
      console.log("id cliente" + idCliente);
      if (idCliente && !isNaN(idCliente)) {
        axios.get(`/clientes/${idCliente}`)
          .then(response => {
            this.loggedUser = response.data;
            console.log("logged user =", JSON.stringify(this.loggedUser, null, 2));
            this.checkIfAdmin();
          })
          .catch(error => {
            console.error('Error al cargar el perfil:', error.response ? error.response.data : error.message);
          });
      } else {
        console.error('No se ha encontrado una ID de cliente válida.');
      }
    },
    //revisar si es admin el usuario que inicio sesion
    checkIfAdmin() {
      const role = this.loggedUser.rol;
      console.log("role " + role);
      if (role === 'admin') {
        this.isAdmin = true;
        this.fetchClientes();
      }
    },
    //modificar el perfil: NO-ADMIN
    updateUserProfile(user) {
      const cli = { ...user };
      const updatedUser = {
        id_cliente: cli.id_cliente,
        nombre: cli.nombre,
        apellido: cli.apellido,
        username: cli.username,
        password: cli.password,
        //rol: cli.rol,
      };
      console.log("updated user: " + updatedUser);
      axios.put(`/clientes/${user.id_cliente}`, updatedUser)
        .then(() => {
          alert('Perfil actualizado con éxito. Debe volver a iniciar sesión.');
          this.editProfile = false;
          auth.logout();
          this.$router.push("/");
        })
    },
    //Actualizar el cliente. FUNCION DE ADMIN
    updateClient(user) {//recibo el selected User
      const cli = { ...user }; //hago en cli una copia del selected User
      const updatedUser = {
        id_cliente: cli.id_cliente,
        nombre: cli.nombre,
        apellido: cli.apellido,
        username: cli.username,
        password: cli.password,
        rol: cli.rol,
      }
      axios.put(`/clientes/${updatedUser.id_cliente}`, updatedUser)
        .then(() => {
          alert('Usuario actualizado con éxito.');
          this.fetchClientes();
          this.clearSelectedUser();
        })
        .catch(error => {
          console.error('Error al actualizar el usuario:', error);
        });
    },

    createCliente() {
      if (this.isAdmin) {
        axios.post('/clientes', this.nuevoCliente)
          .then(() => {
            alert('Usuario creado con éxito.');
            this.nuevoCliente = { nombre: '', apellido: '', username: '', password: '', rol: '' };
            this.fetchClientes();
          })
          .catch(error => {
            console.error('Error al crear el usuario:', error);
          });
      }
    },

    fetchClientes() {
      if (this.isAdmin) {
        axios.get('/clientes')
          .then(response => {
            this.clientes = response.data;
          })
          .catch(error => {
            console.error('Error al obtener los nombres de usuario:', error);
          });
      }
    },

    editSelectedUser(usuario) {
      this.selectedUser = { ...usuario };
      this.verFormMod = true;
      console.log("Usuario seleccionado:", this.selectedUser);
    },
    /*deleteUser(idCliente) {
      axios.delete(`/clientes/${idCliente}`)
        .then(() => {
          alert('Usuario eliminado con éxito.');
          this.fetchClientes();
        })
        .catch(error => {
          console.error('Error al eliminar el usuario:', error.response ? error.response.data : error.message);
        });
    },*/
    deleteUser(idCliente) {
      axios.delete(`/clientes/${idCliente}`)
        .then(() => {
          alert('Usuario eliminado con éxito.');
          this.fetchClientes();
        })
        .catch(error => {
          if (error.response && error.response.status === 409) { // Conflicto (tiene préstamos)
            alert('No se puede eliminar el cliente, ya que tiene préstamos asociados.');
          } else {
            console.error('Error al eliminar el usuario:', error.response ? error.response.data : error.message);
            alert('Error al intentar eliminar el cliente.');
          }
        });
    },
    clearSelectedUser() {
      this.selectedUser = {
        nombre: '',
        apellido: '',
        username: '',
        password: '',
        rol: '',
      }
    },
  },
};
</script>

<style scoped>
.custom-bg {
  background-color: #939cdb;
}
</style>
