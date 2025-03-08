import { createRouter, createWebHistory} from 'vue-router';
import Routes from '@/components/Routes.vue';

import AppProfilePage from '@/components/UI/ProfilePage.vue';
import Home from "@/components/Home.vue";

const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: AppProfilePage },
    { path: '/routes', component: Routes},
    { path: '/home', component: Home},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;