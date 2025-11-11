<template>
  <div class="add-route-wrapper">
    <form class="add-route-form" @submit.prevent="handleSubmit">
      <div class="title-container">Создание маршрута</div>
      <div class="input-block">
        <div class="input-container">
          <label for="routeFrom">Откуда</label>
          <input
            id="routeFrom"
            v-model="formData.routeFrom"
            placeholder="Москва"
          />
        </div>

        <div class="input-container">
          <label for="routeTo">Куда</label>
          <input
            id="routeTo"
            v-model="formData.routeTo"
            placeholder="Екатеринбург"
          />
        </div>

        <div class="input-container">
          <label for="departureTime">Время отправления</label>
          <input
            id="departureTime"
            v-model="formData.destinationTime"
            type="datetime-local"
          />
        </div>

        <div class="input-container">
          <label for="arrivalTime">Время прибытия</label>
          <input
            id="arrivalTime"
            v-model="formData.arrivalTime"
            type="datetime-local"
          />
        </div>

        <div class="input-container">
          <label for="transport">Транспорт</label>
          <InputSuggestionView
            type="select"
            v-model="formData.transport"
            array-type="transport"
            placeholder="Транспорт"
          />
        </div>

        <div class="input-container">
          <label for="availableSeats">Сколько мест</label>
          <input
            id="availableSeats"
            v-model.number="formData.availableSeats"
            placeholder="123"
          />
        </div>

        <div class="input-container">
          <label for="price">Цена</label>
          <input
            id="price"
            v-model.number="formData.price"
            placeholder="5000.00"
          />
        </div>
      </div>

      <div class="button-container">
        <button type="submit">Создать маршрут</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { adminService } from "@/shared/services/adminService";
import { formatToDatabase } from "@/shared/utils/formatTime";
import InputSuggestionView from "@/components/atom/InputSuggestionView.vue";
import { Route } from "@/shared/types/route";
import notification from "@/shared/plugins/notifications";
import { useFormatUtils } from "@/shared/utils/formatUlils";
import { AxiosError } from "axios";

const formData = ref<Route>({
  routeFrom: "",
  routeTo: "",
  transport: "",
  destinationTime: "",
  arrivalTime: "",
  availableSeats: null,
  price: null,
});

const formatUtils = useFormatUtils();

const handleSubmit = async () => {
  const newFilter = formatUtils.removeEmojiForTransport(formData.value);
  formData.value.transport = newFilter.transport;

  if (!formData.value) {
    notification.error("Не найден объект, который надо создать!");
    return;
  }
  if (Object.values(formData.value).some((v) => v == null || v === "")) {
    notification.error("Заполните все поля!");
    return;
  }
  if (
    isNaN(Number(formData.value.price)) ||
    isNaN(Number(formData.value.availableSeats))
  ) {
    notification.error("Заполните поля 'Цена' и 'Свободные места' числами");
    return;
  }
  try {
    await adminService.addRoute(formData.value);
    notification.success("Маршрут был успешно добавлен!");
    formData.value = {
      routeFrom: "",
      routeTo: "",
      transport: "",
      destinationTime: "",
      arrivalTime: "",
      availableSeats: null,
      price: null,
    };
  } catch (error) {
    const axiosError = error as AxiosError;
    console.error(`${axiosError.message}: `, axiosError.status)
  }
};
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

.add-route-wrapper {
  width: 100%;
  .add-route-form {
    @include mixins.display-column();
    width: 100%;
    background: #fffdfd;
    padding: 1rem;
    border-radius: 16px;
    gap: 1rem;
    box-shadow: 0 0 8px rgba($color: #000000, $alpha: 0.15);
    margin-bottom: 1rem;
    .title-container {
      font-size: 32px;
      font-weight: 600;
      text-align: center;
    }
    .input-block {
      @include mixins.display-column();
      gap: 0.5rem;
      .input-container {
        @include mixins.display-column();
        gap: 0.25rem;
        font-size: 18px;
        input {
          @include mixins.custom-input();
        }
      }
    }
    .button-container {
      button{
        @include mixins.button-clear(colors.$accent, white);
        padding: 0.75rem;
        font-size: 18px;
        border-radius: 16px;
        width: 100%;
        font-weight: 600;
        transition: background 0.3s ease;
        &:hover{
          background: colors.$primary-blue;
        }
      }
    }
  }
}
</style>
