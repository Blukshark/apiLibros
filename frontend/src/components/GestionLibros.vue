<template>
  <v-app>
    <NavBar />
    <v-main>
      <v-container>
        <v-row>
          <v-col>
            <v-btn @click="getAllLibros" color="primary">Ver Todos los Libros</v-btn>
            <v-btn @click="showDialog = true" color="primary">Agregar Nuevo Libro</v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col v-for="libro in libros" :key="libro.id_libro">
            <v-card>
              <v-card-title>{{ libro.titulo }}</v-card-title>
              <v-card-subtitle>{{ libro.autor }}</v-card-subtitle>
              <v-card-actions>
                <v-btn @click="getLibro(libro.id_libro)">Ver Detalles</v-btn>
                <v-btn @click="editLibro(libro.id_libro)">Modificar</v-btn>
                <v-btn @click="deleteLibro(libro.id_libro)">Eliminar</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>

        <v-dialog v-model="showDialog" max-width="500px">
          <v-card>
            <v-card-title>
              <span class="headline">Nuevo Libro</span>
            </v-card-title>
            <v-card-text>
              <v-form>
                <v-text-field v-model="newLibro.titulo" label="Título"></v-text-field>
                <v-text-field v-model="newLibro.autor" label="Autor"></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-btn color="primary" @click="createLibro">Guardar</v-btn>
              <v-btn @click="showDialog = false">Cancelar</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import NavBar from './NavBar.vue';
import axios from '../plugins/axios';

export default {
  components: {
    NavBar
  },
  data() {
    return {
      libros: [],
      newLibro: {
        titulo: '',
        autor: ''
      },
      showDialog: false,
    };
  },
  methods: {
    async getAllLibros() {
      try {
        const response = await axios.get('/api/libros');
        this.libros = response.data;
      } catch (error) {
        console.error('Error al obtener los libros:', error);
      }
    },
    async getLibro(id) {
      try {
        const response = await axios.get(`/api/libros/${id}`);
        // Aquí puedes manejar la respuesta para mostrar detalles del libro
        console.log(response);
      } catch (error) {
        console.error('Error al obtener el libro:', error);
      }
    },
    async createLibro() {
      try {
        await axios.post('/api/libros', this.newLibro);
        this.getAllLibros(); // Actualizar la lista de libros
        this.showDialog = false;
        this.newLibro = { titulo: '', autor: '' }; // Limpiar formulario
      } catch (error) {
        console.error('Error al crear el libro:', error);
      }
    },
    /*async editLibro(id) {
      // Implementa la lógica para editar un libro
    },*/
    async deleteLibro(id) {
      try {
        await axios.delete(`/api/libros/${id}`);
        this.getAllLibros(); // Actualizar la lista de libros
      } catch (error) {
        console.error('Error al eliminar el libro:', error);
      }
    }
  }
};
</script>

<style scoped>
.v-container {
  background-color: #f5f5f5;
  min-height: 90vh;
}
.v-card {
  margin-bottom: 16px;
}
</style>
