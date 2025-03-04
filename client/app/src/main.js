import { createApp } from 'vue'
import App from './App.vue'
import routerApp from './routers/router'
import BootstrapVueNext from 'bootstrap-vue-next';
import { MotionPlugin } from '@vueuse/motion';
import Datepicker from '@vuepic/vue-datepicker';
import components from '@/components/UI';
import './assets/global.css';


import './store/auth.js';
import '@vuepic/vue-datepicker/dist/main.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'
const app = createApp(App);

console.log(components);

app.use(BootstrapVueNext)
app.use(routerApp);
app.use(MotionPlugin);
app.use(Datepicker);

app.mount('#app');


