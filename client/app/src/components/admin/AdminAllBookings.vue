<template lang>
    <div>
        <table class="table-custom">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>ID_Маршрута</th>
                    <th>Откуда</th>
                    <th>Куда</th>
                    <th>Дата</th>
                    <th>Отправление</th>
                    <th>Прибытие</th>
                    <th>Транспорт</th>
                    <th>Цена</th>
                    <th>ID</th>
                    <th>Юзер</th>
                    <th>Статус</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="booking in bookings" :key="booking.id">
                    <td>{{ booking.id }}</td>
                    <td>{{ booking.route.id }}</td>
                    <td>{{ booking.route.routeFrom }}</td>
                    <td>{{ booking.route.routeTo }}</td>
                    <td>{{ booking.route.date }}</td>
                    <td>{{ booking.route.time }}</td>
                    <td>{{ booking.route.arrivalTime }}</td>
                    <td>{{ booking.route.transport }}</td>
                    <td>{{ booking.route.price }}</td>
                    <td>{{ booking.user.id }}</td>
                    <td>{{ booking.user.username }}</td>
                    <td>{{ booking.status }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
<script setup>
import AdminService from '@/services/AdminService';
import { onMounted, ref } from 'vue';


const bookings = ref([]);

const allBookings = async() =>{
    try{
        const response = await AdminService.getAllBookings();
        bookings.value = response.data;
    }
    catch(error){
        console.log(error);
    }
}
onMounted(() => {
    allBookings();
})
</script>
<script>
export default {
    name:"AdminAllBookings"
}
</script>
<style lang="sass">
@import "@/assets/styles/adminObjects/all-users-table"
</style>