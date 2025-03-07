<script>
import BookingService from "@/services/BookingService";


export default {
  name: "BookingContainer",
  data(){
    return {
      bookings: [],
      isLoading: false
    };
  },
  created(){
    this.getBookings();
  },
  methods:{
    async getBookings(){
      this.isLoading = true;
      try {
        const refreshToken = localStorage.getItem("refreshToken");
        const response = await BookingService.getMyBooking(refreshToken);
        this.bookings = response.data;
        console.log(this.bookings);
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
  <div class="flight-card">
    <div class="header">
      <div class="transport-icon">✈️</div>
      <div>
        <div class="info-label">ID-Маршрута</div>
        <div class="info-value">#{{ bookings.id }}</div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value"></div>
      <div class="arrow">→</div>
      <div class="info-value"></div>
    </div>

    <div class="route-info">
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ВЫЛЕТА</span>
        <span class="info-value"></span>
      </div>
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ПОСАДКИ</span>
        <span class="info-value"></span>
      </div>
      <div class="info-item">
        <span class="info-label">ДАТА</span>
        <span class="info-value"></span>
      </div>

      <div class="info-value"><div class="status on-time"></div>
      </div>
      <div>
        <button
            class="cancel-button"
            :class="{loading: isCanceling}"
            @click="cancelBooking"
            :disabled="isCanceling">
          <span v-if="!isCanceling">
            Отменить бронь
          </span>
          <div v-else class="loader"></div>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import '@/assets/styles/bookingcontainer'
</style>