import { createRouter, createWebHistory} from 'vue-router';
import Routes from '@/components/Routes.vue';
import GetBooking from '@/components/GetBooking.vue';

import AppProfile from '@/components/Profile.vue';
import Home from "@/components/Home.vue";

const routes = [
    {path: '/', redirect: '/home'},
    { path: '/profile', component: AppProfile },
    { path: '/routes', component: Routes},
    { path: '/home', component: Home},
    { path: '/profile/bookings/my', component: GetBooking},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;