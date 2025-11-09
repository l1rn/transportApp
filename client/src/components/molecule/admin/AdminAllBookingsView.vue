<template>
  <div class="grid-wrapper">
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">
          ID
        </div>
        <div class="grid-cell">
          ID Маршрута
        </div>
        <div class="grid-cell">
          Откуда
        </div>
        <div class="grid-cell">
          Куда
        </div>
        <div class="grid-cell">
          Время отправления
        </div>
        <div class="grid-cell">
          Время прибытия
        </div>
        <div class="grid-cell">
          Транспорт
        </div>
        <div class="grid-cell">
          Имя пользователя
        </div>
        <div class="grid-cell">
          Статус
        </div>
        <div class="grid-cell">
          Цена
        </div>
        <div class="grid-cell"></div>
      </div>
      <div class="grid-content">
        <template 
        v-for="booking in bookings" 
        :key="booking.id">
          <div class="grid-row">
            <div class="grid-cell">
              {{ booking.id }}
            </div>
            <div class="grid-cell">
              {{ booking.route.id }}
            </div>
            <div class="grid-cell">
              {{ booking.route.routeFrom }}
            </div>
            <div class="grid-cell">
              {{ booking.route.routeTo }}
            </div>
            <div class="grid-cell">
              <div class="time-block">
                <span class="time">{{ formatTime(booking.route.time) }}</span>
                <span class="date">{{ formatDate(getDateSource(booking.route, true)) }}</span>
              </div>
            </div>
            <div class="grid-cell">
              {{ booking.route.transport }}
            </div>
            <div class="grid-cell">
              {{ booking.user.username }}
            </div>
            <div class="grid-cell">
              {{ formatStatus(booking.status) }}
            </div>
            <div class="grid-cell">
              {{ booking.route.price }}
            </div>
            <div class="grid-cell">
              <button :disabled="isCanceled(booking.status)" @click="cancelBooking(booking.id)">
                Отменить
              </button>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<script setup>
import { adminService } from '@/shared/services/adminService';
import { onMounted, ref } from 'vue';
import { formatDate, formatTime, getDateSource } from '@/shared/utils/formatTime';
const bookings = ref([]);

const allBookings = async () => {
  try {
    const response = await adminService.getAllBookings();
    bookings.value = response.data;
  }
  catch (error) {
    console.log(error);
  }
}

const hasCanceled = ref(false);

const isCanceled = (status) => {
  if (status === 'CANCELED') return hasCanceled.value = true

  return hasCanceled.value = false
}

const formatStatus = (type) => {
  switch (type) {
    case "CANCELED": return "ОТМЕНЕН"
    case "BOOKED": return "ЗАБРОНИРОВАН"
    default: "НЕТУ ИНФОРМАЦИИ"
  }
}

const cancelBooking = async (bookingId) => {
  try {
    await adminService.patchBooking(bookingId);
    await allBookings();
  } catch (error) {
    console.error(error);
  }
}

onMounted(() => {
  allBookings();
})
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

$grid-columns: 0.5fr 1.25fr 1.5fr 1.5fr 2fr 2fr 1.25fr 1.5fr 1fr 0.75fr;
.grid-wrapper {}

.grid-table {
  .grid-header,.grid-row {
    display: grid;
    grid-template-columns: $grid-columns;
  }

  .grid-header{
    background: #2C3E50;
    color: white;
    border-radius: 16px 16px 0 0;
  }

  .grid-row{
    background: white;
    &:nth-child(even){
      background: #E8ECF0;
    }
  }
  .grid-cell{
    @include mixins.display-center();
    text-align: center;
    padding: 0.25rem;
  }
}
</style>