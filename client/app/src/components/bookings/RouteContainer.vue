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

<style scoped>
body {
  background: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
}

.flight-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.1);
  padding: 32px;
  max-width: 600px;
  width: 100%;
  position: relative;
}

.status {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 6px 12px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 14px;
}

.status.on-time {
  background: #e3fcef;
  color: #006644;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.transport-icon {
  width: 48px;
  height: 48px;
  background: #f1f3f4;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.route-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-size: 14px;
  color: #5f6368;
  margin-bottom: 4px;
}

.info-value {
  font-size: 18px;
  font-weight: 600;
  color: #202124;
}

.price-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.price {
  font-size: 24px;
  font-weight: 700;
  color: #1a73e8;
}

.book-button {
  background: #1a73e8;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: 0.3s;
}

.book-button:hover {
  background: #1557b0;
}

.route-from-to {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 20px;
  margin-bottom: 24px;
}

.arrow {
  width: 24px;
  height: 24px;
  background: #1a73e8;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}
</style>
<script setup lang="ts">
</script>