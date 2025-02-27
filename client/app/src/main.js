import 'bootstrap/dist/css/bootstrap.min.css';
import { createApp } from 'vue'
import App from './App.vue'
import routerApp from './routers/router'

const app = createApp(App);
app.use(routerApp);
app.mount('#app');
