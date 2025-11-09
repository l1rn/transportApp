<template>
  <div class="space"></div>
  <div
    v-for="route in searchResults?.content"
    :key="route.id"
    class="flight-card"
  >
    <div
      class="status"
      :class="{
        'on-time': route.availableSeats > 0, 
        'closed': route.availableSeats <= 0
      }"
    >
      {{ getStatus(route.availableSeats) }}
    </div>

    <div class="header">
      <div class="transport-icon">
        {{ formatUlils.formatTransportStringToEmoji(route.transport) }}
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
        ➔
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
        @click="bookTheRoute(route.id!)"
      >
        Добавить в корзину
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useRouteStore } from '@/shared/stores/useRouteStore';
import { storeToRefs } from 'pinia';
import { bookingService } from '@/shared/services/bookingService';
import { useModalStore } from '@/shared/stores/useModalStore';
import { AxiosError, HttpStatusCode } from 'axios';
import notification from '@/shared/plugins/notifications';
import { useFormatUtils } from '@/shared/utils/formatUlils';

const modalStore = useModalStore();
const formatUlils = useFormatUtils();

const routeStore = useRouteStore();
const { routeData } = storeToRefs(routeStore)

const searchResults = computed(
  () => routeData.value
);

const bookTheRoute = async (routeId: number) => {
  if(routeData.value?.content[routeId].arrivalTime !== undefined){

  }
  try{
    const response = await bookingService.createBooking(routeId);
    if (response.status === HttpStatusCode.Created){
      notification.success("Ваш маршрут у вас в профиле!");
      routeStore.minusAvailableSeat(routeId);
    }
  }
  catch(error){
    const axiosError = error as AxiosError;
    if(axiosError.status === 500){
      notification.error("Не удалось добавить в корзину!");
    }
    if(axiosError.status === 401 || axiosError.status === 403) {
      modalStore.open('login');
      notification.error("Зарегистрируйтесь!")
    }
  }
}

const getStatus = (seats: number) => {
  return seats > 0 ? 'ОТКРЫТО' : 'ЗАКРЫТО';
};
</script>
<style scoped lang="sass">
@use "../../../assets/styles/molecule/home/route-view"
</style>
