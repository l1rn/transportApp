<script setup>
import BookingService from '@/services/BookingService'
import {defineProps, watch, ref, defineEmits} from 'vue'
import router from '@/routers/router'
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

const emit = defineEmits(['require-auth','update:page']);

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
    await BookingService.addBooking(routeId);
    router.push('/profile');
    alert("Успошное бронировае");
    routes.value = routes.value.map(r => 
      r.id === routeId ? {...r, availableSeats: r.availableSeats - 1} : r
    );
  } catch (error) {
    console.log('Ошибка бронирования!', error);
  }
};

</script>

<template>
  <div class="flight-card" v-for="route in searchResults" :key="route.id">
    <div class="status on-time">ОКТРЫТО</div>

    <div class="header">
      <div class="transport-icon">{{ checkRoutesEmoji(route.transport) }}</div>
      <div>
        <div class="info-label">ID Маршрута</div>
        <div class="info-value">#{{ route.id }}</div>
      </div>
    </div>

    <div class="route-from-to">
      <div class="info-value">{{ route.routeFrom }}</div>
      <div class="arrow">→</div>
      <div class="info-value">{{ route.routeTo }}</div>
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
        <span class="info-value">{{ route.availableSeats }}</span>
      </div>
    </div>

    <div class="price-section">
      <div>
        <div class="info-label">ЦЕНА</div>
        <div class="price">{{ route.price }} р.</div>
      </div>
      <button class="book-button" @click="bookingRoute(route.id, $event)">Забронировать сейчас</button>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-container.sass"
</style>
