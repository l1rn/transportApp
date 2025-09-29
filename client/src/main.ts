import { createApp } from 'vue'
import App from './App.vue'

import BootstrapVueNext from 'bootstrap-vue-next';
import { MotionPlugin } from '@vueuse/motion';
import router from '@/routers/router'

import '@/assets/styles/global.sass';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';
import { createPinia } from 'pinia';

const pinia = createPinia();
const app = createApp(App);

app.use(router);
app.use(pinia);
app.use(MotionPlugin);

app.mount('#app');