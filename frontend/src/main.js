import { createApp } from 'vue'; // Importa Vue v3
import App from './App.vue';

//Importar fuentes de material design
import '@mdi/font/css/materialdesignicons.css';

//import Vue router
import router from './router';

//imports de Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const vuetify = createVuetify({
    components,
    directives,
  })

const app = createApp(App) //crear app
app.use(vuetify) //Usar vuetify
app.use(router) //usar router
app.mount('#app') //Montar
