import { createRouter, createWebHistory} from 'vue-router';
import AllRoutesView from '@/components/organism/AllRoutesView.vue';
import SearchRoutes from '@/components/SearchRoutesView.vue'
import AdminPanelView from '@/components/admin/AdminPanelView.vue';
import HomeView from '@/components/HomeView.vue';
import ProfilePageView from '@/components/organism/ProfilePageView.vue';
import PaymentPageView from '@/components/organism/PaymentPageView.vue';

const routes = [
    {
        path: '/', 
        redirect: '/home'
    },
    { 
        path: '/profile', 
        component: ProfilePageView, 
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
        name: 'payment',
        path: '/payment',
        component: PaymentPageView,
        meta: { 
            title: 'ololoseller',    
            showHeaders: false 
        }
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