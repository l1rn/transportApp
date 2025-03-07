<script>
  import BookingContainer from "@/components/bookings/booking/BookingContainer.vue";

  export default{
    name:'BookingCard',
    components: {BookingContainer},
    data(){
      return{
        routeData:{
          route_id: '',
          route_from: '',
          route_to: '',
          transport: '',
          date: '',
          status: '',
          time: null,
          arrival_time: null,
          price: null,
        },
        isCanceling: false,
      }
    },
    methods:
    {
      cancelBooking()
      {
        this.isCanceling = true;
        setTimeout(()=>{
          this.isCanceling = false;
          alert('Бронирование отменено!');
        }, 1000)
      }

    }
  }
</script>

<template>
  <BookingContainer></BookingContainer>
</template>

<style scoped lang="sass">
</style>