<!-- src/components/LibroList.vue -->
<template>
    <div>
      <h1>Lista de Libros</h1>
      <ul>
        <li v-for="libro in libros" :key="libro.id_libro">{{ libro.titulo }}</li>
      </ul>
    </div>
  </template>
  
  <script>
  import axios from '../axios';
  
  export default {
    data() {
      return {
        libros: [],
      };
    },
    async created() {
      try {
        const response = await axios.get('/libros');
        this.libros = response.data;
      } catch (error) {
        console.error('Error al obtener la lista de libros:', error);
      }
    },
  };
  </script>
  