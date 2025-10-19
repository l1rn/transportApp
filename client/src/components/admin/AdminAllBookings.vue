<template>
  <div class="booking-table">
    <h2 class="table-title">
      Список бронирований
    </h2>
    <table class="table-custom">
      <thead>
        <tr>
          <th>ID</th>
          <th>ID_Пути</th>
          <th>Откуда</th>
          <th>Куда</th>
          <th>Отправление</th>
          <th>Прибытие</th>
          <th>Транспорт</th>
          <th>Цена</th>
          <th>ID_Юзера</th>
          <th>Юзер</th>
          <th>Статус</th>
          <th />
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="booking in bookings"
          :key="booking.id"
        >
          <td>{{ booking.id }}</td>
          <td>{{ booking.route.id }}</td>
          <td>{{ booking.route.routeFrom }}</td>
          <td>{{ booking.route.routeTo }}</td>
          <td class="time-cell">
            <div class="time-block">
              <span class="time">{{ formatTime(booking.route.time) }}</span>
              <span class="date">{{ formatDate(getDateSource(booking.route, true)) }}</span>
            </div>
          </td>
            
          <td class="time-cell">
            <div class="time-block">
              <span class="time">{{ formatTime(booking.route.arrivalTime) }}</span>
              <span class="date">{{ formatDate(getDateSource(booking.route, true)) }}</span>
            </div>
          </td>
          <td>{{ booking.route.transport }}</td>
          <td>{{ booking.route.price }}</td>
          <td>{{ booking.user.id }}</td>
          <td>{{ booking.user.username }}</td>
          <td>{{ formatStatus(booking.status) }}</td>
          <td>
            <button
              :disabled="isCanceled(booking.status)"
              @click="cancelBooking(booking.id)"
            >
              Отменить
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script setup>
import AdminService from '@/services/adminService';
import { onMounted, ref } from 'vue';
import { formatDate, formatTime, getDateSource } from '@/utils/formatTime';
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

const hasCanceled = ref(false);

const isCanceled = (status) => {
  if(status === 'CANCELED') return hasCanceled.value = true

  return hasCanceled.value = false
}

const formatStatus = (type) => {
  switch(type){
    case "CANCELED": return "ОТМЕНЕН"
    case "BOOKED": return "ЗАБРОНИРОВАН"
    default: "НЕТУ ИНФОРМАЦИИ"
  }
}

const cancelBooking = async(bookingId) => {
  try{
    await AdminService.patchBooking(bookingId);
    await allBookings();
  }catch(error){
    console.error(error);
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
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/all-booking-table"
</style>