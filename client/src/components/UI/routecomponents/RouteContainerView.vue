<script setup>
import { ref} from 'vue'
import Notifications from '@/components/UI/NotificationsView.vue';
const checkRoutesEmoji = (transport) =>{
  switch(transport){
    case 'Поезд': return '🚂'
    case 'Авиа': return '✈️'
    case 'Автобус': return '🚌'
    default: return ''
  }
}

const notifications = ref(null);

const getStatus = (seats) => {
  return seats > 0 ? 'ОТКРЫТО' : 'ЗАКРЫТО';
};

</script>

<template>
  <Notifications ref="notifications" />
  <div class="space"></div>
  <div
    v-for="route in searchResults"
    :key="route.id"
    class="flight-card"
  >
    <div
      class="status"
      :class="{'on-time': route.availableSeats > 0, 'closed': route.availableSeats >= 0}"
    >
      {{ getStatus(route.availableSeats) }}
    </div>

    <div class="header">
      <div class="transport-icon">
        {{ checkRoutesEmoji(route.transport) }}
      </div>
      <div>
        <div class="info-label">
          ID Маршрута
        </div>
        <div class="info-value">
          #{{ route.id }}
        </div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value">
        {{ route.routeFrom }}
      </div>
      <div class="arrow">
        →
      </div>
      <div class="info-value">
        {{ route.routeTo }}
      </div>
    </div>

    <div class="route-info">
      <div class="info-item">
        <span class="info-label">ОТПРАВЛЕНИЕ</span>
        <span class="info-value">{{ route.time }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ПРИБЫТИЕ</span>
        <span class="info-value">{{ route.arrivalTime }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ДАТА</span>
        <span class="info-value">{{ route.date }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">ОСТАЛОСЬ МЕСТ</span>
        <span class="info-value">{{ Math.max(0, route.availableSeats) }}
          <span
            v-if="route.availableSeats <= 0"
            class="sold-out-text"
          >(мест нет)</span>
        </span>
      </div>
    </div>

    <div class="price-section">
      <div>
        <div class="price">
          {{ route.price }} р.
        </div>
      </div>
      <button
        :disabled="route.availableSeats <= 0"
        class="book-button"
        @click="bookingRoute(route.id, $event)"
      >
        Забронировать сейчас
      </button>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-container.sass"
</style>
