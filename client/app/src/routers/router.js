import { createRouter, createWebHistory} from 'vue-router';
import AppProfilePage from '@/components/UI/ProfilePage.vue';
import Home from "@/components/Home.vue";
import AllRoutesByFilter from '@/components/AllRoutesByFilter.vue';
import SearchRoutes from '@/components/SearchRoutes.vue'
const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: AppProfilePage, meta:{requiresAuth: true} },
    { path: '/routes', component: AllRoutesByFilter},
    { path: '/routes/search', component: SearchRoutes},
    { path: '/home', component: Home},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;