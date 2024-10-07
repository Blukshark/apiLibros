<template>
  <v-app class="custom-bg">
    <v-container>
      <navBar />
    </v-container>

    <v-container>
      <!-- Botones iniciales -->
      <v-row>
        <v-btn @click="toggleBuscarPorId" color="secondary">Buscar autor por ID</v-btn>
        <v-btn @click="toggleCrearAutor" color="secondary">Crear nuevo autor</v-btn>
        <v-spacer></v-spacer>
        <v-btn @click="toggleVerAutores" color="secondary">Ver todos los autores</v-btn>
        <v-spacer></v-spacer>
      </v-row>
    </v-container>

    <!-- Formulario de búsqueda por ID -->
    <v-row v-if="showBuscarPorId">
      <v-col cols="12" md="6">
        <v-text-field v-model="searchId" label="ID del Autor"></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-btn @click="fetchAutorById" color="primary">Buscar</v-btn>
      </v-col>
    </v-row>

    <!-- Formulario para crear un nuevo autor -->
    <v-form v-if="showCrearAutor">
      <v-container>
        <v-text-field v-model="nuevoAutor.nombre" label="Nombre del Autor"></v-text-field>
        <v-text-field v-model="nuevoAutor.apellido" label="Apellido del Autor"></v-text-field>
        <v-btn @click="createAutor" color="primary">Crear Autor</v-btn>
      </v-container>
    </v-form>

    <table v-if="autores.length > 0 && showAutores" class="simple-table">
      <thead>
      <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="autor in autores" :key="autor.id_autor">
          <td>{{ autor.id_autor }}</td>
          <td>{{ autor.nombre }}</td>
          <td>{{ autor.apellido }}</td>
          <td>
            <!-- Botones estilizados -->
            <v-btn small color="blue" @click="editAutor(autor)">Modificar</v-btn>
            <v-btn small color="red" @click="confirmDelete(autor)">Eliminar</v-btn>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Diálogo de eliminación -->
    <v-dialog v-model="showConfirmarEliminar" max-width="400">
      <v-card>
        <v-card-title class="headline">Confirmar eliminación</v-card-title>
        <v-card-text>
          ¿Estás seguro de eliminar a {{ selectedAutor.nombre }} {{ selectedAutor.apellido }}?
        </v-card-text>
        <v-card-actions>
          <v-btn color="green darken-1" text @click="deleteAutor">Sí</v-btn>
          <v-btn color="red darken-1" text @click="showConfirmarEliminar = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Formulario de modificación -->
    <v-form v-if="showModificarAutor">
      <v-text-field v-model="selectedAutor.nombre" label="Nombre"></v-text-field>
      <v-text-field v-model="selectedAutor.apellido" label="Apellido"></v-text-field>
      <v-btn @click="updateAutor" color="primary">Guardar cambios</v-btn>
    </v-form>
  </v-app>
</template>

<script>
import axios from '../plugins/axios';
import navBar from './NavBar.vue';
import '../assets/styles.css';


export default {
  components: {
    navBar,
  },
  data() {
    return {
      autores: [],
      selectedAutor: null,
      searchId: '',
      nuevoAutor: { nombre: '', apellido: '' },
      showBuscarPorId: false,
      showCrearAutor: false,
      showModificarAutor: false,
      showConfirmarEliminar: false,
      showAutores: false,
    };
  },
  methods: {
    async fetchAutores() {
    try {
      const response = await axios.get('/autores');
      this.autores = response.data;
    } catch (error) {
      console.error('Error buscando autores:', error);
      alert('No se encontraron autores.');
    }
  },
  //buscar autor por id
    async fetchAutorById() {
      try {
        const response = await axios.get(`/autores/${this.searchId}`);
        this.autores = [response.data];
        this.showAutores = true;
      } catch (error) {
        console.error('Error buscando autor:', error);
        alert('No se encontró el autor.');
      }
    },
    //confirmar la eliminacion de autor seleccionado
    confirmDelete(autor) {
      this.selectedAutor = autor;
      this.showConfirmarEliminar = true;
    },
    async deleteAutor() {
      try {
        await axios.delete(`/autores/${this.selectedAutor.id_autor}`);
        this.showConfirmarEliminar = false;
        this.fetchAutores();
      } catch (error) {
        console.error('Error eliminando autor:', error);
        alert('No se pudo eliminar el autor.');
      }
    },
    editAutor(autor) {
      this.selectedAutor = { ...autor };
      this.showModificarAutor = true;
    },
    async updateAutor() {
      try {
        await axios.put(`/autores/${this.selectedAutor.id_autor}`, this.selectedAutor);
        this.showModificarAutor = false;
        this.fetchAutores();
      } catch (error) {
        console.error('Error actualizando autor:', error);
        alert('No se pudo actualizar el autor.');
      }
    },
    async createAutor() {
      try {
        const response = await axios.post('/autores', this.nuevoAutor);
        this.autores.push(response.data);
        this.nuevoAutor = { nombre: '', apellido: '' };
        this.showCrearAutor = false;
      } catch (error) {
        console.error('Error creando autor:', error);
        alert('No se pudo crear el autor.');
      }
    },
    toggleBuscarPorId() {
      this.showBuscarPorId = !this.showBuscarPorId;
    },
    toggleCrearAutor() {
      this.showCrearAutor = !this.showCrearAutor;
    },
    toggleVerAutores() {
    // Alternar la visibilidad de la tabla de autores
    if(this.showAutores)
    {
      this.showAutores = false;
    }
    else 
    {
      this.fetchAutores();
      this.showAutores = true;
      
    }
  },
  },
};
</script>

<style scoped>
.custom-bg {
    background-color: #939cdb;
}
.simple-table {
    width: 50%;
}
</style>
