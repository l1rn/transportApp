import { createRouter, createWebHistory} from 'vue-router';
import PageProfileView from '@/components/organism/PageProfileView.vue';
import AllRoutesByFilter from '@/components/AllRoutesFilterView.vue';
import SearchRoutes from '@/components/SearchRoutesView.vue'
import AdminPanelView from '@/components/admin/AdminPanelView.vue';
import PriceRangeRoutesContainer from '@/components/UI/routecomponents/PriceRangeRoutesView.vue'
import HomeView from '@/components/HomeView.vue';

const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: PageProfileView, meta:{requiresAuth: true} },
    { path: '/routes', component: AllRoutesByFilter },
    { path: '/routes/search', component: SearchRoutes },
    { path: '/routes/price', component: PriceRangeRoutesContainer },
    { path:'/panel/admin', component: AdminPanelView },
    { path: '/home', component: HomeView },

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