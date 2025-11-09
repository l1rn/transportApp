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
import { adminService } from '@/shared/services/adminService';
import { onMounted, ref } from 'vue';
import { formatDate, formatTime, getDateSource } from '@/shared/utils/formatTime';
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
    await adminService.patchBooking(bookingId);
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
*
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
.booking-table
  padding: 2rem
  background: #f8f9fa
  border-radius: 12px
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1)

  .table-title
    color: #2c3e50
    font-size: 1.8rem
    margin-bottom: 1.5rem
    text-align: center
    font-weight: 600

  .table-wrapper
    overflow-x: auto
    border-radius: 10px
    background: white
    padding: 1rem
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05)

  .table-custom
    width: 100%
    border-collapse: collapse
    min-width: 1000px
    background: white
    
    th
      background: #3498db
      color: white
      font-weight: 600
      padding: 1.2rem
      text-align: left
      &:first-child
        border-radius: 8px 0 0 0
      &:last-child
        border-radius: 0 8px 0 0
    
    td
      padding: 1rem 1.2rem
      border-bottom: 1px solid #ecf0f1
      transition: background 0.3s ease
    
    tr
      &:nth-child(even)
        background: #f8f9fa
      &:hover
        background: #f1f5f9 !important
      &:last-child
        td
          border-bottom: none
    
    .time-block
      .time
        font-weight: 500
        color: #2c3e50
        font-size: 1.1rem
      .date
        background: #e8f4fd
        color: #3498db
        padding: 0.2rem 0.5rem
        border-radius: 6px
        font-size: 0.85rem
        margin-top: 0.3rem
        display: inline-block
    
    button
      background: #e74c3c
      color: white
      padding: 0.5rem 1rem
      border-radius: 6px
      font-weight: 500
      transition: all 0.3s
      display: flex
      align-items: center
      gap: 0.5rem
      &:hover:not(:disabled)
        background: #c0392b
        transform: translateY(-1px)
        box-shadow: 0 3px 8px rgba(231, 76, 60, 0.3)
      &:disabled
        background: #95a5a6
        cursor: not-allowed

    .status
      display: inline-block
      padding: 0.3rem 0.8rem
      border-radius: 20px
      font-size: 0.9rem
      &[data-status="CANCELED"]
        background: #e74c3c22
        color: #c0392b
      &[data-status="BOOKED"]
        background: #2ecc7122
        color: #27ae60
</style>