<script>

export default {
  name: "BookingContainer",
  props: ['booking'],
  methods: {
    cancel(){
      this.$emit('cancel',this.booking.route.id);
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
        <div class="info-value">#{{ booking.route.id }}</div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value">{{booking.route.routeFrom}}</div>
      <div class="arrow">→</div>
      <div class="info-value">{{booking.route.routeTo}}</div>
    </div>

    <div class="route-info">
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ВЫЛЕТА</span>
        <span class="info-value">{{booking.route.time}}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ВРЕМЯ ПОСАДКИ</span>
        <span class="info-value">{{booking.route.arrivalTime}}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ДАТА</span>
        <span class="info-value">{{booking.route.date}}</span>
      </div>

      <div class="info-value"><div class="status on-time"> {{booking.status}} </div>
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