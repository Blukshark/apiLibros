<template>
  <v-container>
    <navBar />
  </v-container>

  <v-container>
    <!-- Botones iniciales -->
    <v-row>
      <v-btn @click="toggleBuscarPorId">Buscar autor por ID</v-btn>
      <v-btn @click="toggleCrearAutor">Crear nuevo autor</v-btn>
      <v-spacer></v-spacer>
      <v-btn @click="toggleVerAutores">Ver todos los autores</v-btn>
      <v-spacer></v-spacer>
    </v-row>

    <!-- Formulario de búsqueda por ID -->
    <v-row v-if="showBuscarPorId">
      <v-col cols="12" md="6">
        <v-text-field v-model="searchId" label="ID del Autor"></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-btn @click="fetchAutorById">Buscar</v-btn>
      </v-col>
    </v-row>

    <!-- Formulario para crear un nuevo autor -->
    <v-form v-if="showCrearAutor">
      <v-text-field v-model="nuevoAutor.nombre" label="Nombre del Autor"></v-text-field>
      <v-text-field v-model="nuevoAutor.apellido" label="Apellido del Autor"></v-text-field>
      <v-btn @click="createAutor">Crear Autor</v-btn>
    </v-form>

    <!-- Tabla de autores -->
    <v-data-table v-if="autores.length > 0" 
      :headers="headers" 
      :items="autores"
      class="card-style-table">

      <template v-slot:[`item.actions`]="{ item }">
        <v-btn color="blue" @click="editAutor(item)">Modificar</v-btn>
        <v-btn color="red" @click="confirmDelete(item)">Eliminar</v-btn>
      </template>
    </v-data-table>

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
      <v-btn @click="updateAutor">Guardar cambios</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import axios from '../plugins/axios'; // Importo axios
import navBar from './NavBar.vue'; // Importo mi navbar

export default {
  components: {
    navBar,
  },
  data() {
    return {
      autores: [], // Lista de autores que se mostrará en la tabla
      selectedAutor: null, // Autor seleccionado para edición o eliminación, por defecto ninguno
      searchId: '', // ID del autor para la búsqueda
      headers: [ // Encabezados de la tabla
        { text: 'ID', value: 'id_autor' },
        { text: 'Nombre', value: 'nombre' },
        { text: 'Apellido', value: 'apellido' },
        { text: 'Acciones', value: 'actions', sortable: false },
      ],
      nuevoAutor: { // Datos para poder crear un nuevo autor
        nombre: '',
        apellido: ''
      },
      showBuscarPorId: false, // Controla la visibilidad del formulario de búsqueda
      showModificarAutor: false, // Controla la visibilidad del formulario de modificación
      showConfirmarEliminar: false, // Controla la visibilidad del diálogo de eliminación
      showCrearAutor: false,
    };
  },
  methods: {
    // Método para obtener todos los autores
    async fetchAutores() {
      try {
        const response = await axios.get('/autores');
        this.autores = response.data;
        this.showBuscarPorId = false;
        this.showCrearAutor = false;
      } catch (error) {
        console.error('Error buscando autores:', error);
        alert('No se encontraron autores, por favor intente más tarde.');
      }
    },
    // Método para buscar un autor por ID
    async fetchAutorById() {
      try {
        const response = await axios.get(`/autores/${this.searchId}`);
        this.autores = [response.data]; // Mostrar el autor en la tabla
        this.showBuscarPorId = false;
      } catch (error) {
        console.error('Error buscando autor por ID:', error);
        alert('No se encontró el autor con el ID proporcionado.');
      }
    },
    // Método para mostrar el diálogo de confirmación de eliminación
    confirmDelete(autor) {
      this.selectedAutor = autor;
      this.showConfirmarEliminar = true;
    },
    // Método para eliminar un autor
    async deleteAutor() {
      try {
        await axios.delete(`/autores/${this.selectedAutor.id_autor}`);
        this.showConfirmarEliminar = false;
        this.fetchAutores(); // Refrescar la lista después de eliminar
      } catch (error) {
        console.error('Error eliminando autor:', error);
        alert('No se pudo eliminar el autor. Inténtelo de nuevo más tarde.');
      }
    },
    // Método para mostrar el formulario de edición
    editAutor(autor) {
      this.selectedAutor = { ...autor }; // Copiar datos del autor a modificar, spread operator
      this.showModificarAutor = true;
    },
    // Método para actualizar un autor
    async updateAutor() {
      try {
        await axios.put(`/autores/${this.selectedAutor.id_autor}`, this.selectedAutor);
        this.showModificarAutor = false;
        this.fetchAutores(); // Refrescar la lista después de modificar
      } catch (error) {
        console.error('Error actualizando autor:', error);
        alert('No se pudo actualizar el autor. Inténtelo de nuevo más tarde.');
      }
    },
    // Método para crear un nuevo autor
    async createAutor() {
      try {
        const response = await axios.post('/autores', this.nuevoAutor);
        this.autores.push(response.data); // Agregar el nuevo autor a la lista
        this.nuevoAutor = { nombre: '', apellido: '' }; // Limpiar el formulario
        this.showCrearAutor = false; // Ocultar el formulario después de crear el autor
      } catch (error) {
        console.error('Error creando autor:', error);
        alert('No se pudo crear el autor. Inténtelo de nuevo más tarde.');
      }
    },
    // Alternar la visibilidad del formulario de búsqueda por ID
    toggleBuscarPorId() {
      this.showBuscarPorId = !this.showBuscarPorId;
      if (this.showBuscarPorId) {
        this.showCrearAutor = false;
        this.showModificarAutor = false;
      }
    },
    // Alternar la visibilidad del formulario de crear nuevo autor
    toggleCrearAutor() {
      this.showCrearAutor = !this.showCrearAutor;
      if (this.showCrearAutor) {
        this.showBuscarPorId = false;
        this.showModificarAutor = false;
      }
    },
    // Alternar la visibilidad de la tabla de autores
    toggleVerAutores() {
      this.fetchAutores();
      this.showBuscarPorId = false;
      this.showCrearAutor = false;
      this.showModificarAutor = false;
    },
  },
};
</script>

<style scoped>
.card-style-table .v-data-table__tbody tr {
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 8px;
  transition: box-shadow 0.3s;
}

.card-style-table .v-data-table__tbody tr:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
