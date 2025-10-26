<template>
  <div class="booking-wrapper">
    <div 
    v-if="modalStore.isOpen('confirm-payment-form')"
    class="confirm-payment-container">
      <modal-form-view 
        :icon="keyIcon"
        title="Введите код подтверждения"
        desc="Введите код, чтобы подтвердить заказ. 
        Для подтверждения, введите этот код снизу. 
        Код был отправлен на ваш привязаный email"
        button-name="Подтвердить"
        v-model="codeValue"
        input-placeholder="123456"
        input-type="text"
        store-key="confirm-payment-form"
        :submit-func="confirmPayment"
      />
    </div>
    <template 
    v-for="booking in bookings" 
    :key="booking.id">
      <div class="booking-container">
        <div class="header-container">
          <div class="first-section">
            {{ booking.route.transport }}
            <span class="id">
              ID#{{ booking.id }}
            </span>
          </div>
          <div class="second-section">
            {{ booking.status }}
          </div>
        </div>
        <div class="place-container">
          <div class="route">
            {{ booking.route.routeFrom }}
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
              {{ booking.route.time }}
            </div>
          </span>
          <span class="arrival-time">
            <div class="text">
              Время прилета
            </div>
            <div class="content">
              {{ booking.route.arrivalTime }}
            </div>
          </span>
        </div>
        <div class="last-section">
          <div class="date-container">
            <div class="text">
              Дата
            </div>
            {{ booking.route.date }}
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
          <button @click.stop="modalStore.open('confirm-payment-form')">
            Оплатить
          </button>
          <button>Отменить</button>
          <button>История</button>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import keyIcon from "../../../assets/icons/key.svg";
import { bookingService } from '@/services/bookingService';
import { BookingResponse } from '@/types/booking';
import { Ref } from 'vue';
import { onMounted, ref } from 'vue';
import ModalFormView from '@/components/atom/ModalFormView.vue';
import notification from "@/plugins/notifications";
import { AxiosError } from "axios";
import { useModalStore } from "@/stores/useModalStore";

const bookings: Ref<BookingResponse[]> = ref([]);
const isLoading = ref(false);
const codeValue = ref<string>("");

const modalStore = useModalStore();

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

const confirmPayment = async(): Promise<void> => {
  if(!codeValue.value || codeValue.value.length < 6){
    notification.error("Введите достоверный код!");
    return
  }
  try{
    
  }
  catch(e){
    const axiosError = e as AxiosError;
    console.log(axiosError);
  }
}

onMounted(async () => {
  await getBookings();
});
</script>

<style scoped lang="scss">
@import "../../../assets/styles/molecule/profile/bookings-wrapper.scss";
</style>
