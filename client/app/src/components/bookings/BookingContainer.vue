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
        <span class="info-label">ОТКУДА</span>
        <span class="info-value">{{routeData.time}}</span>
      </div>
      <div class="info-item">
        <span class="info-label">КУДА</span>
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

<style scoped>
body {
  background: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 40px;
}

.flight-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.1);
  padding: 42px;
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


.cancel-button {
  background: #fff;
  color: #dc3545;
  border: 2px solid #dc3545;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.cancel-button:hover:not(:disabled) {
  background: #dc3545;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
}

.cancel-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loader {
  width: 20px;
  height: 20px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #dc3545;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>