<template>
  <div class="booking-wrapper">
    <template>
      <div class="booking-container">
        
      </div>
    </template>
  </div>
</template>

<script setup>
import { bookingService } from '@/services/bookingService';
import { onMounted, ref } from 'vue';

const bookings = ref([]);
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

onMounted(async() => {
  await getBookings();
});
</script>

<style scoped lang="sass">
</style>
