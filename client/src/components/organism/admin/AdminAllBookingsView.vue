<template>
  <div class="grid-wrapper">
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">ID</div>
        <div class="grid-cell">Откуда</div>
        <div class="grid-cell">Куда</div>
        <div class="grid-cell">Отправление</div>
        <div class="grid-cell">Прибытие</div>
        <div class="grid-cell">Транспорт</div>
        <div class="grid-cell">Пользователь</div>
        <div class="grid-cell">Статус</div>
        <div class="grid-cell">Цена</div>
        <div class="grid-cell"></div>
      </div>
      <div class="grid-content">
        <template v-for="booking in bookings?.content" :key="booking.id">
          <div class="grid-row">
            <div class="grid-cell">
              {{ booking.id }}
            </div>
            <div class="grid-cell">
              {{ booking.routeFrom }}
            </div>
            <div class="grid-cell">
              {{ booking.routeTo }}
            </div>
            <div class="grid-cell">
              <div class="time-block">
                <span class="time">
                  {{ booking.destinationTime.split('T')[1] }}
                </span>
                <span class="date">
                  {{ booking.destinationTime.split('T')[0] }}
                </span>
              </div>
            </div>
            <div class="grid-cell">
              <div class="time-block">
                <span class="time">
                  {{ booking.arrivalTime.split('T')[1] }}
                </span>
                <span class="date">
                  {{ booking.arrivalTime.split('T')[0] }}
                </span>
              </div>
            </div>
            <div class="grid-cell">
              {{ booking.transport }}
            </div>
            <div class="grid-cell">
              {{ booking.username }}
            </div>
            <div class="status-cell">
              <div class="status-text">
                {{ formatStatus(booking.status) }}
              </div>
              <div class="status-icon">
                <img :src="formatUtils.getStatusIconFromBookingStatus(booking.status)" alt="">
              </div>
            </div>
            <div class="grid-cell">
              {{ booking.price }}
            </div>
            <div class="grid-cell">
              <button 
              class="cancel-button"
              :disabled="booking.status === 'CANCELLED'" 
              @click="cancelBooking(booking.id)">
                Отменить
              </button>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="button-container">
        <button @click="handleBackward" :disabled="page <= 0">
          Назад
        </button>
        <div class="page-container">
          <div class="text">Страница {{ page + 1 }} из {{ bookings?.totalPages }}</div>
          <a @click="resetPage">Сбросить?</a>
        </div>
        <button @click="handleForward" :disabled="page >= bookings?.totalPages! - 1">
          Вперед
        </button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { adminService } from "@/shared/services/adminService";
import { onMounted, ref } from "vue";
import { PaginatedResponse } from "@/shared/types/response";
import { AdminGetAllBookings } from "@/shared/types/booking";
import { useFormatUtils } from "@/shared/utils/formatUlils";

const page = ref(0);
const formatUtils = useFormatUtils();
const bookings = ref<PaginatedResponse<AdminGetAllBookings> | null>(null);

const loadBookings = async (page: number = 0) => {
  try {
    const response = await adminService.getAllBookings(page = page);
    bookings.value = response.data;
    console.log(bookings.value);
  } catch (error) {
    console.log(error);
  }
};

const handleForward = () => {
  if (page.value >= bookings.value?.totalPages!) {
    return;
  }
  page.value++;
  loadBookings(page.value);
}

const handleBackward = () => {
  if (page.value <= 0) {
    return;
  }
  page.value--;
  loadBookings(page.value);
}

const resetPage = () => {
  if(page.value === 0) return;
  page.value = 0;
  loadBookings(page.value);
}

const formatStatus = (type: string) => {
  switch (type) {
    case "PAID":
      return "ОПЛАЧЕН";
    case "CANCELLED":
      return "ОТМЕНЕН";
    case "PENDING":
      return "В ОБРАБОТКЕ";
    default:
      "НЕТУ ИНФОРМАЦИИ";
  }
};

const cancelBooking = async (bookingId: number) => {
  try {
    await adminService.patchBooking(bookingId);
    await loadBookings();
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  loadBookings();
});
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

$grid-columns: 0.3fr 1.5fr 1.5fr 1fr 1fr 1.25fr 1.5fr 1.5fr 0.75fr 1fr;

.grid-wrapper {
  @include mixins.display-column();
  gap: 1rem;
  .page-container{
    display: flex;
    gap: 0.5rem;
    padding: 0.5rem;
    background: colors.$light-grey;
    border-radius: 16px;
    a{
      color: rgb(62, 130, 194);
      text-decoration: none;
      cursor: pointer;
      &:hover{
        text-decoration: underline;
      }
    }
  }

  .button-container {
    @include mixins.display-center();
    margin-bottom: 1rem;
    gap: 2rem;

    button {
      @include mixins.button-clear(colors.$accent, white);
      padding: 0.7rem 1.5rem;
      font-size: 16px;
      font-weight: 600;
      border-radius: 8px;
      transition: all 0.3s ease;
      &:hover{
        background: colors.$primary-blue;
      }
      &:disabled{
        background: colors.$medium-grey;
      }
    }
  }
}

.grid-table {

  .grid-header,
  .grid-row {
    display: grid;
    grid-template-columns: $grid-columns;
  }

  .grid-header {
    background: #2c3e50;
    color: white;
    border-radius: 16px 16px 0 0;

    .grid-cell {
      font-weight: 600;
    }
  }

  .grid-row {
    background: white;
    transition: all 0.3s ease;

    &:nth-child(even) {
      background: #e8ecf0;
    }

    &:last-child {
      border-radius: 0 0 16px 16px;
    }

    &:hover {
      background: #e9e9ff;
    }
  }

  .grid-cell {
    @include mixins.display-center();
    text-align: center;
    padding: 0.5rem;
    gap: 0.25rem;

    .time-block {
      @include mixins.display-column();

      .date {
        background: #ccc;
        font-size: 12px;
        padding: 2px 4px;
        border-radius: 8px;
      }
    }
    .cancel-button{
      @include mixins.button-clear(#ff6b6b, white);
      padding: 0.5rem;
      font-size: 1.025rem;
      border-radius: 8px;
      transition: all 0.3s ease;
      &:hover{
        background: #e74c3c;
      }
      &:disabled{
        background: colors.$medium-grey;
        cursor: not-allowed;
      }
    }
  }

  .status-cell {
    @include mixins.display-center();

    .status-text {
      width: 80%;
      text-align: center;
    }

    .status-icon {
      display: flex;

      img {
        width: 20px;
      }
    }
  }
}
</style>
