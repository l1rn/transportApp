import { createApp } from 'vue'
import App from './App.vue'
import router from '@/routers/router'

import '@/assets/styles/global.sass';
import { createPinia } from 'pinia';

const pinia = createPinia();
const app = createApp(App);

app.use(router);
app.use(pinia);

app.mount('#app');