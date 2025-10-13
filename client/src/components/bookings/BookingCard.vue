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

<script setup>
import { ref, onMounted } from 'vue';
import BookingContainer from './BookingContainer.vue';
import BookingService, { bookingService } from '@/services/bookingService';

const bookings = ref([]);
const isLoading = ref(false);
const cancelingIds = ref(new Set());

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

onMounted(() => {
  setTimeout(() => {
    getBookings()
  }, 500) 
})
</script>

<style scoped lang="sass">
</style>
