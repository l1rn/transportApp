<!-- eslint-disable vue/multi-word-component-names -->
<template>
   
    <div class="container">
        <h1 class="text-center"> Все маршруты</h1>
        <table class="table table-striped">
            <thead>
                <th>from</th>
                <th>to</th>
                <th>time from</th>
                <th>arrival time</th>
            </thead>
            <tbody>
                 <tr v-for="route in routes" :key="route.id">
                    <td>{{ route.id }}</td> 
                    <td>{{ route.routeFrom }}</td> 
                    <td>{{ route.routeTo }}</td>
                    <td>{{ route.time }}</td>
                    <td>{{ route.arrivalTime }}</td>
                    <button @click="addbook()">book</button>
                 </tr>   
            </tbody>
        </table>
    </div>
    
</template>
<script>
import RoutesService from '@/services/RoutesService';
import BookingService from '@/services/BookingService'
export default {
    name: 'AppRoutes',
    data(){
        return{
            routeId: 1,
            userId: 1,
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
                const token = localStorage.getItem('token');
                const response = await BookingService.addBooking(this.routeId, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
            }
            catch(error){
                console.log(error.message);
            } 
        }
    },
    created(){
        this.getRoutes();
    }
}
</script>
<style lang="">
    
</style>