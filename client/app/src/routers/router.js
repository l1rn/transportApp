import { createRouter, createWebHistory} from 'vue-router';
import Routes from '@/components/Routes.vue';
import GetBooking from '@/components/GetBooking.vue';

import Profile from '@/components/Profile.vue';
import App from '@/App.vue';

const routes = [
    {path: '/', redirect: '/home'},
    { path: '/routes', component: Routes},
    { path: '/home', component: App},
    { path: '/profile', component: Profile},
    { path: '/profile/bookings/my', component: GetBooking},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;