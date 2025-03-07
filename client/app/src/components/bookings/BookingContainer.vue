<script>
  export default{
    name:'BookingContainer',
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
  <div class="flight-card">
    <div class="header">
      <div class="transport-icon">✈️</div>
      <div>
        <div class="info-label">ID-Маршрута</div>
        <div class="info-value">#{{routeData.route_id}}</div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value">{{routeData.route_from}}</div>
      <div class="arrow">→</div>
      <div class="info-value">{{routeData.route_to}}</div>
    </div>

    <div class="route-info">
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ВЫЛЕТА</span>
        <span class="info-value">{{routeData.time}}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ПОСАДКИ</span>
        <span class="info-value">{{routeData.arrival_time}}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ДАТА</span>
        <span class="info-value">{{routeData.date}}</span>
      </div>

      <div class="info-value"><div class="status on-time">{{routeData.status}}</div>
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
@import '@/assets/styles/bookingcontainer.sass'
</style>