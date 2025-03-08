<template>
    <div>
        <h1>1234</h1>
        <button @click="getAllMyBooking">get</button>
        <ul>
            <li v-for="book in bookings" :key="book.id">{{ book.id }} {{ book.route.routeFrom }} {{ book.route.routeTo }} {{ book.route.time }} - {{ book.route.arrivalTime }} {{ book.status }}
            </li>
        </ul>
    </div>
</template>
<script>
import BookingService from '@/services/BookingService';
export default {
    name: 'AppGetMyBooking',
    data(){
        return{
            bookings: []
        }
    },
    methods:{
        getAllMyBooking(){
                const config = {
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                    'Content-type': 'application/json'
                    }
                };
                
            try{
                BookingService.getMyBooking(config).then((response) =>{
                    this.bookings = response.data;
                });
            }
            catch(error){
                console.log("unluck to get your booking");
            }
        }
    }
}
</script>
<style lang="">
    
</style>