<!-- eslint-disable vue/multi-word-component-names -->
<template>
   
    <div class="container">
        <BNavbar>
            <BNavbarBrand class="brand" href="/routes">ololotravel</BNavbarBrand>
            <BNavbarNav>
                <BNavItemDropdown text="Профиль" right>
                    <BDropdownItem href="/auth/sign-up">Регистрация</BDropdownItem>
                    <BDropdownItem href="/auth/sign-in">Авторизация</BDropdownItem>
                    <BDropdownItem href="/auth/logout">Выйти</BDropdownItem>
                    <BDropdownItem href="/auth/profile">Профиль</BDropdownItem>
                </BNavItemDropdown>
            </BNavbarNav>
        </BNavbar>
        <table class="table table-striped">
            <thead>
                <th>откуда</th>
                <th>куда</th>
                <th>время посадки</th>
                <th>время прибытия</th>
                <th>свободных мест</th>
            </thead>
            <tbody>
                 <tr v-for="route in routes" :key="route.id">
                    <td>{{ route.routeFrom }}</td> 
                    <td>{{ route.routeTo }}</td>
                    <td>{{ route.time }}</td>
                    <td>{{ route.arrivalTime }}</td>
                    <td>{{ route.availableSeats }}</td>
                    <button @click="addbook()">book</button>
                 </tr>   
            </tbody>
        </table>
    </div>
    
</template>
<script>
import RoutesService from '@/services/RoutesService';
import BookingService from '@/services/BookingService'
import { BDropdownItem, BNavbar, BNavbarBrand, BNavbarNav, BNavItemDropdown } from 'bootstrap-vue-next';
export default {
    name: 'AppRoutes',
    components:{
        BNavbar,
        BNavbarBrand,
        BNavbarNav,
        BNavItemDropdown,
        BDropdownItem
    },
    data(){
        return{
            
            routeId: 1,
            routes: []
        }
    },
    methods:{
        getRoutes(){
            RoutesService.getRoutes().then((response) =>{
                this.routes = response.data
            });
        },
        async addbook(){
            try{
                console.log(localStorage.getItem("token"));

                const token = localStorage.getItem('token');
                const response = await BookingService.addBooking(this.routeId, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
                this.$router.push('/auth/profile');
            }
            catch(error){
                this.$router.push('/auth/sign-up');
                console.log(error.message);
            } 
        }
    },
    created(){
        this.getRoutes();
    }
}
</script>
<style scoped>
/* overflow-x:hidden - chrome, firefox, IE, edge, safari, opera*/
::v-deep .dropdown-menu {
  overflow-y: hidden; 
  scrollbar-width: none;
  -ms-overflow-style: none; 
}

::v-deep .dropdown-menu::-webkit-scrollbar {
  display: none;
}
.container{
    font-family: Montserrat;
}
.brand{
    color: rgb(27, 27, 27);
    font-size: large;
}
</style>