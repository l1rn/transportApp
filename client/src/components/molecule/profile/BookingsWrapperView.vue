<template>
  <div class="booking-wrapper">
    <template 
    v-for="booking in bookings" 
    :key="booking.id">
      <div class="booking-container">
        <div class="header-container">
          <div class="first-section">
            <div class="transport-icon">
              {{ formatUtils.formatTransportStringToEmoji(booking.route.transport) }}
            </div>
            <span class="id-container">
              <div class="top-string">
                ID Маршрута
              </div>
              <div class="botton-string">
                #{{ booking.id }}
              </div>
            </span>
          </div>
          <div 
          class="second-section"
          :class="{ 
            'paid': booking.status === 'PAID',
            'pending': booking.status === 'PENDING',
            'canceled': booking.status === 'CANCELED'
          }">
            {{ formatUtils.formatBookingStatus(booking.status) }}
          </div>
        </div>
        <div class="place-container">
          <div class="route">
            {{ booking.route.routeFrom }}
          </div>
          <div class="arrow-container">
            ➔
          </div>
          <div class="route">
            {{ booking.route.routeTo }}
          </div>
        </div>
        <div class="time-container">
          <span class="departure-time">
            <div class="text">
              Время вылета
            </div>
            <div class="content">
              {{ booking.route.destinationTime.split('T')[1] }}
            </div>
          </span>
          <span class="arrival-time">
            <div class="text">
              Время прилета
            </div>
            <div class="content">
              {{ booking.route.arrivalTime.split('T')[1] }}
            </div>
          </span>
        </div>
        <div class="last-section">
          <div class="date-container">
            <div class="text">
              Дата
            </div>
            {{ booking.route.destinationTime.split('T')[0] }}
          </div>
          <div class="price-container">
            <div class="text">
              Цена
            </div>
            {{ booking.route.price }}
            Р.
          </div>
        </div>

        <div class="button-container">
          <button 
          class="payment-button"
          v-if="booking.status === 'PENDING'"
          @click="handlePayment(booking.id)">
            Оплатить
          </button>
          <button
          v-if="booking.status === 'PENDING'"
          class="cancel-button">
            Отменить
          </button>
          <button
          class="history-button">
            История
          </button>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useProfilePage } from '@/composable/useProfilePage';
import notification from '@/shared/plugins/notifications';
import { bookingService } from '@/shared/services/bookingService';
import { BookingResponse } from '@/shared/types/booking';
import { useFormatUtils } from '@/shared/utils/formatUlils';
import { Ref } from 'vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps<{
  hasEmail: boolean;
}>();

const bookings: Ref<BookingResponse[]> = ref([]);
const isLoading = ref(false);
const { openForm } = useProfilePage();

const router = useRouter();
const formatUtils = useFormatUtils();

const getBookings = async () => {
  isLoading.value = true;
  try {
    const response = await bookingService.getMyBooking();
    bookings.value = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    isLoading.value = false;
  }
};

const handlePayment = (id: number) => {
  if(props.hasEmail === false){
    notification.error("Сначало нужно привязать почту!");
    openForm("profile-page-settings");
    return;
  }
  const routeData = router.resolve({
    name: 'payment',
    query: {
      bookingId: JSON.stringify(id)
    },
  });
  window.open(routeData.href, '_blank');
}

onMounted(async () => {
  await getBookings();
});
</script>

<style scoped lang="scss">
@use "../../../assets/styles/molecule/profile/bookings-wrapper.scss";
</style>
