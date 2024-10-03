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
        
        <!-- v-combobox para seleccionar autor -->
        <v-combobox
        v-model="nuevoLibro.id_autor"
        :items="autores"
        item-text="nombre"
        item-value="id_autor"
        label="Seleccionar Autor"
        required
      ></v-combobox>
      

        <!--Select normal de html-->
        <select>
          <option v-for="autor in autores" :key="autor.id_autor">{{autor.id_autor}} {{ autor.nombre }}{{ autor.apellido }}</option>
        </select>

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
  <td>{{ libro.id_autor }} - {{ libro.autor ? (libro.autor.nombre + ' ' + libro.autor.apellido) : 'Desconocido' }}</td>
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
      
      <v-select
    ></v-select>
      <v-btn @click="updateLibro" color="primary">Guardar cambios</v-btn>
    </v-form>

</v-app>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from '../plugins/axios';
import navBar from './NavBar.vue';
import '../assets/styles.css';

// Variables reactivas
const autores = ref([]);
const libros = ref([]);
const searchId = ref('');
const selectedLibro = reactive({}); // Para mantener reactivo el libro seleccionado
const nuevoLibro = reactive({ titulo: '',id_autor:'', genero: '', num_paginas: 0, sinopsis: '', fecha_publicacion: '', stock: 0 });
const showConfirmarEliminar = ref(false);
const showModificarLibro = ref(false);
const showCrearLibro = ref(false);
const showBuscarPorId = ref(false);
const showLibros = ref(false);

// Funciones

const fetchAutores = async () => {
  try {
    const response = await axios.get('/autores');
    console.log(response.data); // Asegúrate de que la API devuelva datos
    autores.value = response.data.map(autor => ({
      ...autor,
      nombreCompleto: `${autor.nombre} ${autor.apellido}`
    }));
  } catch (error) {
    console.error('Error buscando autores:', error);
    alert('No se encontraron autores.');
  }
};




const fetchLibros = async () => {
  try {
    // Obtener todos los libros
    const response = await axios.get('/libros');
    libros.value = response.data;

    // Iterar sobre los libros y obtener los autores asociados
    await Promise.all(
      libros.value.map(async (libro) => {
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
};

const fetchLibroById = async () => {
  try {
    const response = await axios.get(`/libros/${searchId.value}`);
    const libro = response.data;

    // Obtener el autor del libro
    try {
      const responseAutor = await axios.get(`/autores/${libro.id_autor}`);
      libro.autor = responseAutor.data; // Añadir el autor al libro
    } catch (error) {
      console.error(`Error buscando autor para el libro con id_autor ${libro.id_autor}:`, error);
      libro.autor = { nombre: 'Desconocido', apellido: '' }; // En caso de error, asignar valores predeterminados
    }

    libros.value = [libro]; // Actualizar la lista de libros con el libro encontrado
    showLibros.value = true;
  } catch (error) {
    console.error('Error buscando libro:', error);
    alert('No se encontró el libro.');
  }
};


const confirmDelete = (libro) => {
  selectedLibro.id_libro = libro.id_libro;
  showConfirmarEliminar.value = true;
};

const deleteLibro = async () => {
  try {
    await axios.delete(`/libros/${selectedLibro.id_libro}`);
    showConfirmarEliminar.value = false;
    fetchLibros();
  } catch (error) {
    console.error('Error eliminando libro:', error);
    alert('No se pudo eliminar el libro.');
  }
};

const editLibro = (libro) => {
  Object.assign(selectedLibro, libro); // Copia los datos del libro seleccionado
  showModificarLibro.value = true;
};

const updateLibro = async () => {
  try {
    await axios.put(`/libros/${selectedLibro.id_libro}`, selectedLibro);
    showModificarLibro.value = false;
    fetchLibros();
  } catch (error) {
    console.error('Error actualizando libro:', error);
    alert('No se pudo actualizar el libro.');
  }
};

const createLibro = async () => {
  try {
    const response = await axios.post('/libros', nuevoLibro);
    libros.value.push(response.data);
    // Resetear el libro nuevo
    Object.assign(nuevoLibro, { titulo: '', genero: '', num_paginas: 0, sinopsis: '', fecha_publicacion: '', stock: 0 });
    showCrearLibro.value = false;
  } catch (error) {
    console.error('Error creando libro:', error);
    alert('No se pudo crear el libro.');
  }
};

const toggleBuscarPorId = () => {
  showBuscarPorId.value = !showBuscarPorId.value;
};

const toggleCrearLibro = () => {
  console.log(autores.value);
  showCrearLibro.value = !showCrearLibro.value;
};

const toggleVerLibros = () => {
  showLibros.value = true;
  if (showLibros.value) {
    fetchLibros();
  }
};

// Llamar a fetchAutores cuando el componente esté montado
onMounted(() => {
  fetchAutores();
});

</script>


<style scoped>
.custom-bg{
  background-color: #939cdb;
}
</style>