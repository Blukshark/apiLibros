<template>
  <v-app class="custom-bg">
    <v-container>
      <navBar />
    </v-container>

    <v-container>
      <!-- Botones iniciales -->
      <v-row>
        <v-btn @click="toggleBuscarPorId" color="secondary">Buscar préstamo por ID</v-btn>
        <v-btn @click="toggleCrearPrestamo" color="secondary">Crear nuevo préstamo</v-btn>
        <v-spacer></v-spacer>
        <v-btn @click="toggleVerPrestamos" color="secondary">Ver todos los préstamos</v-btn>
        <v-spacer></v-spacer>
      </v-row>
    </v-container>

    <!-- Formulario de búsqueda por ID -->
    <v-row v-if="showBuscarPorId">
      <v-col cols="12" md="6">
        <v-text-field v-model="searchId" label="ID del Préstamo"></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-btn @click="fetchPrestamoById" color="primary">Buscar</v-btn>
      </v-col>
    </v-row>

    <!-- Formulario para crear un nuevo préstamo -->
    <v-form v-if="showCrearPrestamo">
      <v-container>
        <v-select
          v-model="nuevoPrestamo.id_cliente"
          :items="clientes"
          item-title="nombre"
          item-value="id_cliente"
          label="Selecciona un cliente"
        />
        <v-select
          v-model="nuevoPrestamo.id_libro"
          :items="libros"
          item-title="titulo"
          item-value="id_libro"
          label="Selecciona un libro"
        />
        <v-text-field v-model="nuevoPrestamo.fecha_inicio" label="Fecha de Inicio" type="date"></v-text-field>
        <v-text-field v-model="nuevoPrestamo.fecha_fin" label="Fecha de Fin" type="date"></v-text-field>
        <v-btn @click="createPrestamo" color="primary">Crear Préstamo</v-btn>
      </v-container>
    </v-form>

    <!-- Tabla de préstamos -->
    <table v-if="prestamos.length > 0 && showPrestamos" class="simple-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>ID Cliente</th>
          <th>ID Libro</th>
          <th>Fecha de Inicio</th>
          <th>Fecha de Fin</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="prestamo in prestamos" :key="prestamo.id_prestamo">
          <td>{{ prestamo.id_prestamo }}</td>
          <td>{{ prestamo.id_cliente }}</td>
          <td>{{ prestamo.id_libro }}</td>
          <td>{{ prestamo.fecha_inicio }}</td>
          <td>{{ prestamo.fecha_fin || 'Pendiente' }}</td>
          <td>
            <v-btn small color="blue" @click="editPrestamo(prestamo)">Modificar</v-btn>
            <v-btn small color="red" @click="confirmDelete(prestamo)">Eliminar</v-btn>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Diálogo de eliminación -->
    <v-dialog v-model="showConfirmarEliminar" max-width="400">
      <v-card>
        <v-card-title class="headline">Confirmar eliminación</v-card-title>
        <v-card-text>
          ¿Estás seguro de eliminar el préstamo con ID "{{ selectedPrestamo.id_prestamo }}"?
        </v-card-text>
        <v-card-actions>
          <v-btn color="green darken-1" text @click="deletePrestamo">Sí</v-btn>
          <v-btn color="red darken-1" text @click="showConfirmarEliminar = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Formulario de modificación -->
    <v-form v-if="showModificarPrestamo">
      <v-select
        v-model="selectedPrestamo.id_cliente"
        :items="clientes"
        item-title="nombre"
        item-value="id_cliente"
        label="Selecciona un cliente"
      />
      <v-select
        v-model="selectedPrestamo.id_libro"
        :items="libros"
        item-title="titulo"
        item-value="id_libro"
        label="Selecciona un libro"
      />
      <v-text-field v-model="selectedPrestamo.fecha_inicio" label="Fecha de Inicio" type="date"></v-text-field>
      <v-text-field v-model="selectedPrestamo.fecha_fin" label="Fecha de Fin" type="date"></v-text-field>
      <v-btn @click="updatePrestamo" color="primary">Guardar cambios</v-btn>
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
  mounted() {
    this.fetchClientes();
    this.fetchLibros();
    this.fetchPrestamos();
  },
  data() {
    return {
      prestamos: [],
      clientes: [],
      libros: [],
      selectedPrestamo: null,
      searchId: '',
      nuevoPrestamo: {
        id_cliente: '',
        id_libro: '',
        fecha_inicio: '',
        fecha_fin: '',
      },
      showBuscarPorId: false,
      showCrearPrestamo: false,
      showModificarPrestamo: false,
      showConfirmarEliminar: false,
      showPrestamos: false,
    };
  },
  methods: {
    async fetchClientes() {
      try {
        const response = await axios.get('/clientes');
        this.clientes = response.data;
      } catch (error) {
        console.error('Error buscando clientes:', error);
        alert('No se encontraron clientes.');
      }
    },
    async fetchLibros() {
      try {
        const response = await axios.get('/libros');
        this.libros = response.data;
      } catch (error) {
        console.error('Error buscando libros:', error);
        alert('No se encontraron libros.');
      }
    },
    async fetchPrestamos() {
      try {
        const response = await axios.get('/prestamos');
        this.prestamos = response.data;
      } catch (error) {
        console.error('Error buscando préstamos:', error);
        alert('No se encontraron préstamos.');
      }
    },
    async fetchPrestamoById() {
      try {
        const response = await axios.get(`/prestamos/${this.searchId}`);
        const prestamo = response.data;

        if (prestamo) {
          this.prestamos = [prestamo];
          this.showPrestamos = true;
        } else {
          alert('No se encontró el préstamo.');
        }
      } catch (error) {
        console.error('Error buscando préstamo:', error);
        alert('No se encontró el préstamo.');
      }
    },
    confirmDelete(prestamo) {
      this.selectedPrestamo = prestamo;
      this.showConfirmarEliminar = true;
    },
    async deletePrestamo() {
      try {
        await axios.delete(`/prestamos/${this.selectedPrestamo.id_prestamo}`);
        this.showConfirmarEliminar = false;
        this.fetchPrestamos();
      } catch (error) {
        console.error('Error eliminando préstamo:', error);
        alert('No se pudo eliminar el préstamo.');
      }
    },
    editPrestamo(prestamo) {
      this.selectedPrestamo = { ...prestamo };
      this.showModificarPrestamo = true;
    },
    async updatePrestamo() {
      try {
        await axios.put(`/prestamos/${this.selectedPrestamo.id_prestamo}`, this.selectedPrestamo);
        this.showModificarPrestamo = false;
        this.fetchPrestamos();
      } catch (error) {
        console.error('Error actualizando préstamo:', error);
        alert('No se pudo actualizar el préstamo.');
      }
    },
    async createPrestamo() {
      try {
        const response = await axios.post('/prestamos', this.nuevoPrestamo);
        this.prestamos.push(response.data);
        this.nuevoPrestamo = { id_cliente: '', id_libro: '', fecha_inicio: '', fecha_fin: '' };
        this.showCrearPrestamo = false;
      } catch (error) {
        console.error('Error creando préstamo:', error);
        alert('No se pudo crear el préstamo.');
      }
    },
    toggleBuscarPorId() {
      this.showBuscarPorId = !this.showBuscarPorId;
    },
    toggleCrearPrestamo() {
      this.showCrearPrestamo = !this.showCrearPrestamo;
    },
    toggleVerPrestamos() {
      this.showPrestamos = !this.showPrestamos;
      if (this.showPrestamos) {
        this.fetchPrestamos();
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
