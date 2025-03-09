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
        time: null,
        arrival_time: null,
        price: null,
      }
    }
  },
  methods:
      {

      }
}
</script>

<template>
  <div class="flight-card">
    <div class="status on-time">ON TIME</div>

    <div class="header">
      <div class="transport-icon">✈️</div>
      <div>
        <div class="info-label">ROUTE ID</div>
        <div class="info-value">#FL-2345</div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value">New York (JFK)</div>
      <div class="arrow">→</div>
      <div class="info-value">London (LHR)</div>
    </div>

    <div class="route-info">
      <div class="info-item">
        <span class="info-label">DEPARTURE</span>
        <span class="info-value">09:45 AM</span>
      </div>
      <div class="info-item">
        <span class="info-label">ARRIVAL</span>
        <span class="info-value">10:30 PM</span>
      </div>
      <div class="info-item">
        <span class="info-label">DATE</span>
        <span class="info-value">15 Oct 2023</span>
      </div>
      <div class="info-item">
        <span class="info-label">SEATS AVAILABLE</span>
        <span class="info-value">24</span>
      </div>
    </div>

    <div class="price-section">
      <div>
        <div class="info-label">PRICE</div>
        <div class="price">$450</div>
      </div>
      <button class="book-button">Book Now</button>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-container.sass"
</style>
