<template>
  <v-app class="custom-bg">
    <v-container>
      <navBar />
    </v-container>

    <v-container>
      <!-- Botones iniciales -->
      <v-row>
        <v-btn @click="toggleBuscarPorId" color="secondary">Buscar libro por ID</v-btn>
        <v-btn @click="toggleCrearLibro" color="secondary">Crear nuevo libro</v-btn>
        <v-spacer></v-spacer>
        <v-btn @click="toggleVerLibros" color="secondary">Ver todos los libros</v-btn>
        <v-spacer></v-spacer>
      </v-row>
    </v-container>

    <!-- Formulario de búsqueda por ID -->
    <v-row v-if="showBuscarPorId">
      <v-col cols="12" md="6">
        <v-text-field v-model="searchId" label="ID del Libro"></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-btn @click="fetchLibroById" color="primary">Buscar</v-btn>
      </v-col>
    </v-row>

    <!-- Formulario para crear un nuevo libro -->
    <v-form v-if="showCrearLibro">
      <v-container>
        <v-text-field v-model="nuevoLibro.titulo" label="Título del Libro"></v-text-field>
        <v-text-field v-model="nuevoLibro.genero" label="Género"></v-text-field>
        <v-text-field v-model="nuevoLibro.num_paginas" label="Número de Páginas" type="number"></v-text-field>
        <v-text-field v-model="nuevoLibro.sinopsis" label="Sinopsis"></v-text-field>
        <v-text-field v-model="nuevoLibro.fecha_publicacion" label="Fecha de Publicación" type="date"></v-text-field>
        <v-text-field v-model="nuevoLibro.stock" label="Stock" type="number"></v-text-field>

        <!--v-select para elegir un autor-->
        <v-select v-model="nuevoLibro.id_autor" :items="autores" item-title="nombre" item-value="id_autor"
          label="Selecciona un autor">
        </v-select>

        <v-btn @click="createLibro" color="primary">Crear Libro</v-btn>

      </v-container>
    </v-form>

    <!-- Tabla de libros -->
    <table v-if="libros.length > 0 && showLibros" class="simple-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>ID Autor</th>
          <th>Título</th>
          <th>Género</th>
          <th>Número de Páginas</th>
          <th>Sinopsis</th>
          <th>Fecha de Publicación</th>
          <th>Stock</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="libro in libros" :key="libro.id_libro">
          <td>{{ libro.id_libro }}</td>
          <td>{{ libro.id_autor }} - {{ libro.autor ? libro.autor.nombre : 'Cargando...' }} {{ libro.autor ?
            libro.autor.apellido : '' }}</td>
          <td>{{ libro.titulo }}</td>
          <td>{{ libro.genero }}</td>
          <td>{{ libro.num_paginas }}</td>
          <td>{{ libro.sinopsis }}</td>
          <td>{{ libro.fecha_publicacion }}</td>
          <td>{{ libro.stock }}</td>
          <td>
            <v-btn small color="blue" @click="editLibro(libro)">Modificar</v-btn>
            <v-btn small color="red" @click="confirmDelete(libro)">Eliminar</v-btn>
          </td>
        </tr>

      </tbody>
    </table>


    <!-- Diálogo de eliminación -->
    <v-dialog v-model="showConfirmarEliminar" max-width="400">
      <v-card>
        <v-card-title class="headline">Confirmar eliminación</v-card-title>
        <v-card-text>
          ¿Estás seguro de eliminar el libro "{{ selectedLibro.titulo }}"?
        </v-card-text>
        <v-card-actions>
          <v-btn color="green darken-1" text @click="deleteLibro">Sí</v-btn>
          <v-btn color="red darken-1" text @click="showConfirmarEliminar = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Formulario de modificación -->
    <v-form v-if="showModificarLibro">
      <v-text-field v-model="selectedLibro.titulo" label="Título"></v-text-field>
      <v-text-field v-model="selectedLibro.genero" label="Género"></v-text-field>
      <v-text-field v-model="selectedLibro.num_paginas" label="Número de Páginas" type="number"></v-text-field>
      <v-text-field v-model="selectedLibro.sinopsis" label="Sinopsis"></v-text-field>
      <v-text-field v-model="selectedLibro.fecha_publicacion" label="Fecha de Publicación" type="date"></v-text-field>
      <v-text-field v-model="selectedLibro.stock" label="Stock" type="number"></v-text-field>
      <v-select v-model="selectedLibro.id_autor" :items="autores" item-title="nombre" item-value="id_autor"
        label="Selecciona un autor">
      </v-select>
      <v-btn @click="updateLibro" color="primary">Guardar cambios</v-btn>
    </v-form>
  </v-app>
</template>

<script>
import axios from '../plugins/axios';
import navBar from './NavBar.vue';
import '../assets/styles.css';
//import fetchAutorById from './GestionAutores.vue';
export default {
  components: {
    navBar,
  },
  mounted() {
    this.fetchAutores();
  },
  data() {
    return {
      libros: [],
      autores: [],
      //autor:[],
      selectedAutor: {
        id_autor: '',
        nombre: '',
        apellido: ''
      },
      selectedLibro: null,
      searchId: '',
      nuevoLibro: {
        titulo: '',
        genero: '',
        num_paginas: 0,
        sinopsis: '',
        fecha_publicacion: '',
        stock: 0,
      },
      showBuscarPorId: false,
      showCrearLibro: false,
      showModificarLibro: false,
      showConfirmarEliminar: false,
      showLibros: false,
    };
  },
  methods: {
    //buscar autores
    async fetchAutores() {
      try {
        const response = await axios.get('/autores');
        this.autores = response.data;
        console.log(this.autores);
      } catch (error) {
        console.error('Error buscando autores:', error);
        alert('No se encontraron autores.');
      }
    },
    async fetchLibros() {
      try {
        // Obtener todos los libros
        const response = await axios.get('/libros');
        this.libros = response.data;

        // Iterar sobre los libros y obtener los autores asociados
        await Promise.all(
          this.libros.map(async (libro) => {
            try {
              const responseAutor = await axios.get(`/autores/${libro.id_autor}`);
              libro.autor = responseAutor.data; // Añadir el autor a cada libro
            } catch (error) {
              console.error(`Error buscando autor para el libro con id_autor ${libro.id_autor}:`, error);
              libro.autor = { nombre: 'Desconocido', apellido: '' }; // En caso de error, asignar valores predeterminados
            }
          })
        );
      } catch (error) {
        console.error('Error buscando libros:', error);
        alert('No se encontraron libros.');
      }
    },
    async fetchLibroById() {
      try {
        const response = await axios.get(`/libros/${this.searchId}`);
        const libro = response.data;

        // Ahora obtenemos el autor para el libro consultado
        try {
          const responseAutor = await axios.get(`/autores/${libro.id_autor}`);
          libro.autor = responseAutor.data; // Añadimos el autor al libro
        } catch (error) {
          console.error(`Error buscando autor para el libro con id_autor ${libro.id_autor}:`, error);
          libro.autor = { nombre: 'Desconocido', apellido: '' }; // En caso de error, asignar valores predeterminados
        }

        this.libros = [libro]; // Asignamos el libro obtenido a la lista de libros
        this.showLibros = true;
      } catch (error) {
        console.error('Error buscando libro:', error);
        alert('No se encontró el libro.');
      }
    },
    confirmDelete(libro) {
      this.selectedLibro = libro;
      this.showConfirmarEliminar = true;
    },
    async deleteLibro() {
      try {
        await axios.delete(`/libros/${this.selectedLibro.id_libro}`);
        this.showConfirmarEliminar = false;
        this.fetchLibros();
      } catch (error) {
        console.error('Error eliminando libro:', error);
        alert('No se pudo eliminar el libro.');
      }
    },
    editLibro(libro) {
      this.selectedLibro = { ...libro };
      this.showModificarLibro = true;
    },
    async updateLibro() {
      try {
        await axios.put(`/libros/${this.selectedLibro.id_libro}`, this.selectedLibro);
        this.showModificarLibro = false;
        this.fetchLibros();
      } catch (error) {
        console.error('Error actualizando libro:', error);
        alert('No se pudo actualizar el libro.');
      }
    },
    async createLibro() {
      try {
        const response = await axios.post('/libros', this.nuevoLibro);
        this.libros.push(response.data);
        this.nuevoLibro = { titulo: '', genero: '', num_paginas: 0, sinopsis: '', fecha_publicacion: '', stock: 0 };
        this.showCrearLibro = false;
      } catch (error) {
        console.error('Error creando libro:', error);
        alert('No se pudo crear el libro.');
      }
    },
    toggleBuscarPorId() {
      this.showBuscarPorId = !this.showBuscarPorId;
    },
    toggleCrearLibro() {
      console.log(this.autores);
      this.showCrearLibro = !this.showCrearLibro;
    },
    toggleVerLibros() {
      this.showLibros = !this.showLibros;
      if (this.showLibros) {
        this.fetchLibros();
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
