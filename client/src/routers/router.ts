import { createRouter, createWebHistory} from 'vue-router';
import HomeView from '@/components/HomeView.vue';
import ProfilePageView from '@/components/page/ProfilePageView.vue';
import PaymentPageView from '@/components/page/PaymentPageView.vue';
import AllRoutesView from '@/components/page/AllRoutesView.vue';
import SearchRoutesView from '@/components/page/SearchRoutesView.vue';
import AdminPanelView from '@/components/page/AdminPanelView.vue';

const routes = [
    {
        path: '/', 
        redirect: '/home'
    },
    { 
        path: '/profile', 
        component: ProfilePageView, 
        meta: {
            requiresAuth: true
        } 
    },
    { 
        path: '/routes', 
        component: AllRoutesView,
        meta: {
            requiresAuth: false
        }
    },
    { 
        path: '/routes/search', 
        component: SearchRoutesView,
        meta: {
            requiresAuth: false
        }
    },
    { 
        path:'/panel/admin', 
        component: AdminPanelView,
        meta: {
            requiresAuth: true
        }
    },
    { 
        path: '/home', 
        component: HomeView, 
        meta: { 
            showHeaders: false,
            requiresAuth: false
        }
    },
    {
        name: 'payment',
        path: '/payments',
        component: PaymentPageView,
        meta: { 
            title: 'ololoseller',    
            showHeaders: false,
            requiresAuth: true
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