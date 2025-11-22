<template>
  <PaymentHistoryView v-if="isHistoryOpen" :payment-history="paymentHistory!" :page="page"
    :is-history-open="isHistoryOpen" @close="handleModalClose" @forward="handleForward" @backward="handleBackward" />
  <div class="booking-wrapper">
    <template v-for="booking in bookings" :key="booking.id">
      <div class="booking-container">
        <div class="header-container">
          <div class="first-section">
            <div class="transport-icon">
              {{
                formatUtils.formatTransportStringToEmoji(
                  booking.route.transport
                )
              }}
            </div>
            <span class="id-container">
              <div class="top-string">ID Маршрута</div>
              <div class="botton-string">#{{ booking.id }}</div>
            </span>
          </div>
          <div class="second-section" :class="booking.status">
            {{ formatUtils.formatBookingStatus(booking.status) }}
          </div>
        </div>

        <div class="route-section">
          <div class="city from">{{ booking.route.routeFrom }}</div>
          <div class="line-container">
            <div class="line"></div>
          </div>
          <div class="city to">{{ booking.route.routeTo }}</div>
        </div>

        <div class="details-section">
          <div class="detail-item">
            <span class="detail-icon">🕒</span>
            <div class="detail-content">
              <div class="detail-label">Вылет</div>
              <div class="detail-value">
                {{ booking.route.destinationTime.split("T")[0] }} {{  booking.route.destinationTime.split("T")[1] }}
              </div>
            </div>
          </div>
          <div class="detail-item">
            <span class="detail-icon">🕒</span>
            <div class="detail-content">
              <div class="detail-label">Прилет</div>
              <div class="detail-value">
                {{ booking.route.arrivalTime.split("T")[0] }} {{  booking.route.arrivalTime.split("T")[1] }}
              </div>
            </div>
          </div>
        </div>

        <div class="footer-section">
          <div class="price-display">
            <div class="price-label">Итого</div>
            <div class="price-value">{{ booking.route.price }} Р.</div>
          </div>
          <div class="button-container">
            <button class="payment-button" v-if="booking.status === 'PENDING'" @click="handlePayment(booking.id)">
              Оплатить
            </button>
            <button v-if="booking.status === 'PENDING'" class="cancel-button">
              Отменить
            </button>
            <button @click.stop="handlePaymentHistory(booking.id)" class="history-button">
              История
            </button>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useProfilePage } from "@/composable/useProfilePage";
import notification from "@/shared/plugins/notifications";
import { bookingService } from "@/shared/services/bookingService";
import { paymentService } from "@/shared/services/paymentService";
import { BookingResponse } from "@/shared/types/booking";
import { PaymentHistoryResponse } from "@/shared/types/payment";
import { PaginatedResponse } from "@/shared/types/response";
import { useFormatUtils } from "@/shared/utils/formatUlils";
import { AxiosError } from "axios";
import { Ref, watchEffect } from "vue";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import PaymentHistoryView from "./PaymentHistoryView.vue";

const props = defineProps<{
  hasEmail: boolean;
}>();

const page = ref(0);
const isHistoryOpen = ref(false);
const paymentHistory = ref<PaginatedResponse<PaymentHistoryResponse>>();

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
  if (props.hasEmail === false) {
    notification.error("Сначало нужно привязать почту!");
    openForm("profile-page-settings");
    return;
  }
  const routeData = router.resolve({
    name: "payment",
    query: {
      bookingId: JSON.stringify(id),
    },
  });
  window.open(routeData.href, "_blank");
};

const handleForward = () => {
  if (!paymentHistory.value) return;
  if (page.value >= paymentHistory.value?.totalPages - 1) return;
  page.value++;
};

const handleBackward = () => {
  if (!paymentHistory.value) return;
  if (page.value <= 0) return;
  page.value--;
};

const handleModalClose = () => {
  isHistoryOpen.value = false;
};

const handlePaymentHistory = async (bookingId: number) => {
  try {
    const response =
      await paymentService.getPaymentHistoryByBookingId(bookingId);
    paymentHistory.value = response.data;
    isHistoryOpen.value = true;
    console.log(paymentHistory.value);
  } catch (e) {
    const axiosError = e as AxiosError;
  }
};

onMounted(async () => {
  await getBookings();
});

watchEffect(() => {
  if (isHistoryOpen.value) {
    document.body.style.overflow === "hidden";
    document.documentElement.style.overflowY = "hidden";
  } else {
    document.body.style.overflowY = "auto";
    document.documentElement.style.overflowY = "auto";
  }
});
</script>

<style scoped lang="scss">
@use "../../../assets/styles/molecule/profile/bookings-wrapper.scss";
</style>
