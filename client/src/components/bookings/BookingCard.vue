<script setup>
import UserService from "@/services/UserService";

import { ref, onMounted } from 'vue';
import BookingContainer from './BookingContainer.vue';
import BookingService from '@/services/BookingService';
import router from "@/routers/router";

const bookings = ref([]);
const isLoading = ref(false);
const cancelingIds = ref(new Set());

const getBookings = async () => {
  isLoading.value = true;
  try {
    const response = await BookingService.getMyBooking();
    bookings.value = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    isLoading.value = false;
  }
};

const cancelBooking = async (bookingId) => {
  cancelingIds.value.add(bookingId);
  try {
    await BookingService.cancelMyBooking(bookingId);
    bookings.value = bookings.value.map(b => {
      if (b.id === bookingId) {
        return { ...b, status: 'CANCELED' };
      }
      return b;
    });
  } catch (error) {
    console.error(error);
  } finally {
    cancelingIds.value.delete(bookingId);
  }
};

const checkAuth = async() => {
  try{
    await UserService.checkAuth();
  } catch{
    try{
      await UserService.refreshIfCheckAuth()
    }catch{
      router.push('/');
    }
  }
}
onMounted(() => {
  checkAuth(),
  getBookings()
})
</script>
<template>
  <div class="d-flex flex-column align-items-center">
    <BookingContainer
      v-for="booking in bookings"
      :key="booking.id"
      class="mb-3"
      :booking="booking"
      :is-canceling="cancelingIds.has(booking.id)"
      @cancel-booking-event="cancelBooking"
    />
  </div>
</template>

<style scoped lang="sass">
</style>
