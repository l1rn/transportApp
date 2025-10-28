import { createRouter, createWebHistory} from 'vue-router';
import PageProfileView from '@/components/organism/PageProfileView.vue';
import AllRoutesView from '@/components/organism/AllRoutesView.vue';
import SearchRoutes from '@/components/SearchRoutesView.vue'
import AdminPanelView from '@/components/admin/AdminPanelView.vue';
import HomeView from '@/components/HomeView.vue';

const routes = [
    {
        path: '/', 
        redirect: '/home'
    },
    { 
        path: '/profile', 
        component: PageProfileView, 
        meta:{requiresAuth: true} 
    },
    { 
        path: '/routes', 
        component: AllRoutesView 
    },
    { 
        path: '/routes/search', 
        component: SearchRoutes 
    },
    { 
        path:'/panel/admin', 
        component: AdminPanelView 
    },
    { 
        path: '/home', 
        component: HomeView, 
        meta: { showHeaders: false }
    },
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