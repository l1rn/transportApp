import { createRouter, createWebHistory} from 'vue-router';
import ProfilePage from '@/components/UI/ProfilePageView.vue';
import Home from "@/components/HomeView.vue";
import AllRoutesByFilter from '@/components/AllRoutesFilterView.vue';
import SearchRoutes from '@/components/SearchRoutesView.vue'
import AdminPanel from '@/components/admin/AdminPanelView.vue';
import PriceRangeRoutesContainer from '@/components/UI/routecomponents/PriceRangeRoutesView.vue'
const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: ProfilePage, meta:{requiresAuth: true} },
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
    history: createWebHistory(),
    routes
})

export default router;