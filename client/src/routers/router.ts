import { createRouter, createWebHistory} from 'vue-router';
import AdminPanelView from '@/components/admin/AdminPanelView.vue';
import HomeView from '@/components/HomeView.vue';
import ProfilePageView from '@/components/page/ProfilePageView.vue';
import PaymentPageView from '@/components/page/PaymentPageView.vue';
import AllRoutesView from '@/components/page/AllRoutesView.vue';
import SearchRoutesView from '@/components/page/SearchRoutesView.vue';

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
        component: SearchRoutesView
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
        path: '/payments',
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