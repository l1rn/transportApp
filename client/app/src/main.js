import { createApp } from 'vue'
import App from './App.vue'

import BootstrapVueNext from 'bootstrap-vue-next';
import { MotionPlugin } from '@vueuse/motion';
import Datepicker from '@vuepic/vue-datepicker';
import router from '@/routers/router'

import '@/assets/styles/global.sass';
import '@vuepic/vue-datepicker/dist/main.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

const app = createApp(App);
app.use(BootstrapVueNext)
app.use(router);
app.use(MotionPlugin);
app.use(Datepicker);

app.mount('#app');