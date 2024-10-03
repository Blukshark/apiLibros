import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import 'vuetify/styles'; // Importa los estilos de Vuetify

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'mdi', // Aquí aseguras que se usen los iconos Material Design Icons
  },
});
