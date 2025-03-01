import { createRouter, createWebHistory} from 'vue-router';
import Routes from '@/components/Routes.vue';
import GetBooking from '@/components/GetBooking.vue';
import Signup from '@/components/Signup.vue';
import Signin from '@/components/Signin.vue';
import Profile from '@/components/Profile.vue';

const routes = [
    {path: '/', redirect: '/routes'},
    { path: '/routes', component: Routes},
    { path: '/profile', component: Profile},
    { path: '/profile/bookings/my', component: GetBooking},
    { path: '/auth/sign-up', component: Signup},
    { path: '/auth/sign-in', component: Signin}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;