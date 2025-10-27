<template>
  <div class="booking-wrapper">
    <template 
    v-for="booking in bookings" 
    :key="booking.id">
      <div class="booking-container">
        <div class="header-container">
          <div class="first-section">
            {{ booking.route.transport }}
            <span class="id">
              ID#{{ booking.id }}
            </span>
          </div>
          <div class="second-section">
            {{ booking.status }}
          </div>
        </div>
        <div class="place-container">
          <div class="route">
            {{ booking.route.routeFrom }}
          </div>
          <div class="route">
            {{ booking.route.routeTo }}
          </div>
        </div>
        <div class="time-container">
          <span class="departure-time">
            <div class="text">
              Время вылета
            </div>
            <div class="content">
              {{ booking.route.time }}
            </div>
          </span>
          <span class="arrival-time">
            <div class="text">
              Время прилета
            </div>
            <div class="content">
              {{ booking.route.arrivalTime }}
            </div>
          </span>
        </div>
        <div class="last-section">
          <div class="date-container">
            <div class="text">
              Дата
            </div>
            {{ booking.route.date }}
          </div>
          <div class="price-container">
            <div class="text">
              Цена
            </div>
            {{ booking.route.price }}
            Р.
          </div>
        </div>

        <div class="button-container">
          <button>
            Оплатить
          </button>
          <button>Отменить</button>
          <button>История</button>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { bookingService } from '@/services/bookingService';
import { BookingResponse } from '@/types/booking';
import { Ref } from 'vue';
import { onMounted, ref } from 'vue';

const bookings: Ref<BookingResponse[]> = ref([]);
const isLoading = ref(false);

const getBookings = async () => {
  isLoading.value = true;
  try {
    const response = await bookingService.getMyBooking();
    bookings.value = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  await getBookings();
});
</script>

<style scoped lang="scss">
@import "../../../assets/styles/molecule/profile/bookings-wrapper.scss";
</style>
