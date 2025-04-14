import { createRouter, createWebHistory} from 'vue-router';
import AppProfilePage from '@/components/UI/ProfilePage.vue';
import Home from "@/components/Home.vue";
import AllRoutesByFilter from '@/components/AllRoutesByFilter.vue';
import SearchRoutes from '@/components/SearchRoutes.vue'
import AdminPanel from '@/components/admin/AdminPanel.vue';
import PriceRangeRoutesContainer from '@/components/UI/routecomponents/PriceRangeRoutesContainer.vue'
const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: AppProfilePage, meta:{requiresAuth: true} },
    { path: '/routes', component: AllRoutesByFilter},
    { path: '/routes/search', component: SearchRoutes},
    { path: '/routes/price', component: PriceRangeRoutesContainer},
    { path:'/panel/admin', component: AdminPanel},
    { path: '/home', component: Home},

    { 
        path: '/:catchAll(.*)',
        redirect: '/home'
    }
]

const router = createRouter({
    history: createWebHistory('/transportApp'),
    routes
})

export default router;