<template>
  <div class="add-route-wrapper">
    <form class="add-route-form" @submit.prevent="handleSubmit">
      <div class="title-container">Создание маршрутов</div>
      <div class="input-container">
        <label for="routeFrom">Откуда</label>
        <input
          id="routeFrom"
          v-model="formData.routeFrom"
          required
          @input="clearError"
        />
      </div>

      <div class="input-container">
        <label for="routeTo">Куда</label>
        <input
          id="routeTo"
          v-model="formData.routeTo"
          required
          @input="clearError"
        />
      </div>

      <label for="departureTime">Время отправления</label>
      <div class="input-container">
        <input
          id="departureTime"
          v-model="formData.departureTime"
          type="datetime-local"
          required
          @change="updateDepartureTime"
        />
      </div>

      <div class="input-container">
        <label for="arrivalTime">Время прибытия</label>
        <input
          id="arrivalTime"
          v-model="formData.arrivalTime"
          type="datetime-local"
          required
          :min="formData.departureTime"
          @change="updateArrivalTime"
        />
      </div>

      <div class="input-container">
        <label for="transport">Транспорт</label>
        <InputSuggestionView 
        type="select"
        :suggestion-list="transportList"
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
          required
        />
      </div>

      <div class="input-container">
        <label for="price">Цена</label>
        <input
          id="price"
          v-model.number="formData.price"
          required
        />
      </div>

      <div class="button-container">
        <button type="submit" :disabled="isSubmitting">
          {{ isSubmitting ? "Сохранение..." : "Создать маршрут" }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { adminService } from "@/shared/services/adminService";
import { formatToDatabase } from "@/shared/utils/formatTime";
import InputSuggestionView from "../InputSuggestionView.vue";
import { Route } from "@/shared/types/route";

const transportList = ref<Array<string>>([
  "🚌 Автобус",
  "✈️ Авиа",
  "🚆 Поезд",
  "🏍️ Любой"
])

const formData = ref<Partial<Route>>({});

const isSubmitting = ref(false);
const errorMessage = ref("");
const successMessage = ref("");


const clearError = () => {
  errorMessage.value = "";
  successMessage.value = "";
};

const resetForm = () => {
  formData.value = {};
};

const formatDateTime = (datetime: string) => {
  if (!datetime) return "";
  return formatToDatabase(datetime);
};
const handleSubmit = async () => {
  try {
    isSubmitting.value = true;
    clearError();
    const payload = {
      ...formData.value,
      departureTime: formatDateTime(formData.value.departureTime),
      arrivalTime: formatDateTime(formData.value.arrivalTime),
    };

    await adminService.addRoute(payload);

    resetForm();
  } catch (error) {

  } finally {
    isSubmitting.value = false;
  }
};
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

.add-route-wrapper {
  width: 100%;
  .add-route-form {
    width: 100%;
    background: #ccc;
    padding: 1rem;
    border-radius: 16px;
    .input-container {
      @include mixins.display-column();
      input {
        @include mixins.custom-input();
      }
    }
  }
}
</style>
