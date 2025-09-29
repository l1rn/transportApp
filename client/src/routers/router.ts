<<<<<<< HEAD:client/src/routers/router.js
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

=======
import { createRouter, createWebHistory} from 'vue-router';
import ProfilePage from '@/components/UI/ProfilePage.vue';
import Home from "@/components/Home.vue";
import AllRoutesByFilter from '@/components/AllRoutesByFilter.vue';
import SearchRoutes from '@/components/SearchRoutes.vue'
import AdminPanel from '@/components/admin/AdminPanel.vue';
import PriceRangeRoutesContainer from '@/components/UI/routecomponents/PriceRangeRoutesContainer.vue'
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

>>>>>>> 8a6cce314fb11973cf56c7551e6cfb08585b32bb:client/src/routers/router.ts
export default router;