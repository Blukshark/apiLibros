import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import 'vuetify/styles'; // Importar estilos de Vuetify

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'mdi', //Material Design Icons
  },
});