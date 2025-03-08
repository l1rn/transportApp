<script>
import BookingContainer from "@/components/bookings/booking/BookingContainer.vue";
import BookingService from "@/services/BookingService";

export default {
  name: 'BookingCard',
  components: {BookingContainer},
  data(){
    return{
      bookings: [],
      isLoading: false,
      cancelingIds: new Set(),
    }
  },
  created(){
    this.getBookings();
  },
  methods:{
    async getBookings(){
      this.isLoading = true;
      try {
        const response = await BookingService.getMyBooking();
        this.bookings = response.data;
      }
      catch(error){
        console.error(error);
      }
      finally{
        this.isLoading = false;
      }
    },
    async cancelBooking(bookingId){
      this.cancelingIds.add(bookingId);
      try {
        await BookingService.cancelMyBooking(bookingId);
        this.bookings = this.bookings.map(b => {
          if (b.id === bookingId) {
            b.status = 'CANCELED';
          }
          return b;
        });
      } catch(error){
        console.error(error);
      }
      finally {
        this.cancelingIds.delete(bookingId);
      }
    }
  }
}
</script>

<template>
  <div class="d-flex flex-column align-items-center">
    <BookingContainer
        class="mb-3"
        v-for="booking in bookings"
        :key="booking.id"
        :booking="booking"
        :is-canceling="cancelingIds.has(booking.id)"
        @cancelBookingEvent="cancelBooking"
    />
  </div>
</template>

<style scoped lang="sass">
</style>
