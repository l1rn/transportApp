<script setup>
import BookingService from '@/services/BookingService'
import {defineProps, watch, ref, defineEmits} from 'vue'
import Notifications from '@/components/UI/Notifications.vue';
const props = defineProps({
  currentPage: Number,
  itemsPerPage: Number,
  searchResults:{
    type:Array,
    required: true,
    validator: value => value.every(item =>
      'id' in item && 'transport' in item
    )
  }
})
const checkRoutesEmoji = (transport) =>{
  switch(transport){
    case 'Поезд': return '🚂'
    case 'Авиа': return '✈️'
    case 'Автобус': return '🚌'
    default: return ''
  }
}

const emit = defineEmits(['require-auth','update-seats']);

const routes = ref([...props.searchResults])
watch(() => props.searchResults, (newResults) => {
  routes.value = [...newResults]
})


const bookingRoute = async (routeId, event) => { 
  event.preventDefault();
  if (!localStorage.getItem('accessToken')) {
    emit('require-auth');
    return;
  }
  try {
    const route = routes.value.find(r => r.id === routeId);
    if (!route || route.availableSeats <= 0) return;
    await BookingService.addBooking(routeId);
    emit('update-seats', routeId);
    showMessage('success',"Успошное бронирование!");
  } catch (error) {
    showMessage('error', error.response?.data?.message || 'Ошибка бронирования');
  }
};

const notifications = ref(null);

const showMessage = (type, message) => {
  notifications.value?.showNotification(type, message);
};
const getStatus = (seats) => {
  return seats > 0 ? 'ОТКРЫТО' : 'ЗАКРЫТО';
};

</script>

<template>
  <Notifications ref="notifications"></Notifications>
  <div
    v-for="route in searchResults"
    :key="route.id"
    class="flight-card"
  >
    <div class="status"
    :class="{'on-time': route.availableSeats > 0, 'closed': route.availableSeats >= 0}">
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
          <span v-if="route.availableSeats <= 0" class="sold-out-text">(мест нет)</span>
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
