<template>
    <v-data-table
      :headers="headers"
      :items="desserts"
      item-key="name"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Simple Data Table</v-toolbar-title>
        </v-toolbar>
      </template>
    </v-data-table>
  </template>
  
  <script>
  export default {
    data() {
      return {
        headers: [
          { text: 'Dessert (100g serving)', value: 'name' },
          { text: 'Calories', value: 'calories' },
          { text: 'Fat (g)', value: 'fat' },
          { text: 'Carbs (g)', value: 'carbs' },
          { text: 'Protein (g)', value: 'protein' },
        ],
        desserts: [
          { name: 'Frozen Yogurt', calories: 159, fat: 6.0, carbs: 24, protein: 4.0 },
          { name: 'Ice Cream Sandwich', calories: 237, fat: 9.0, carbs: 37, protein: 4.3 },
        ],
      };
    },
  };
  </script>
  