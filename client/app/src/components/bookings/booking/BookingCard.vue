<script>
  import BookingContainer from "@/components/bookings/booking/BookingContainer.vue";
  import BookingService from "@/services/BookingService";

  export default{
    name:'BookingCard',
    components: {BookingContainer},
    data(){
      return{
        bookings: [],
        isLoading:false,
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
          console.log(response.data);
          this.bookings = response.data;
        }
        catch(error){
          console.log(error);
        }
        finally{
          this.isLoading = false;
        }
      },
      async cancelBooking(){

      }
    },
  }
</script>

<template>
  <BookingContainer
  v-for="booking in bookings"
  :key="booking.id"
  :booking="booking"
  ></BookingContainer>
</template>

<style scoped lang="sass">
</style>