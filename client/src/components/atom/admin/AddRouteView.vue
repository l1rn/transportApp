<template>
  <div>
    <div class="add-route-form">
      <form class="input-form" @submit.prevent="handleSubmit">
        <h1>Создание маршрутов</h1>
        <label for="routeFrom">Откуда</label>
        <div>
          <input
            id="routeFrom"
            v-model="formData.routeFrom"
            required
            @input="clearError"
          />
        </div>

        <label for="routeTo">Куда</label>
        <div>
          <input
            id="routeTo"
            v-model="formData.routeTo"
            required
            @input="clearError"
          />
        </div>

        <label for="departureTime">Время отправления</label>
        <div>
          <input
            id="departureTime"
            v-model="formData.departureTime"
            type="datetime-local"
            required
            @change="updateDepartureTime"
          />
        </div>

        <label for="arrivalTime">Время прибытия</label>
        <div>
          <input
            id="arrivalTime"
            v-model="formData.arrivalTime"
            type="datetime-local"
            required
            :min="formData.departureTime"
            @change="updateArrivalTime"
          />
        </div>

        <label for="transport">Транспорт</label>
        <div>
          <select id="transport" v-model="formData.transport" required>
            <option value="Автобус">Автобус</option>
            <option value="Поезд">Поезд</option>
            <option value="Авиа">Авиа</option>
          </select>
        </div>

        <label for="availableSeats">Сколько мест</label>
        <div>
          <input
            id="availableSeats"
            v-model.number="formData.availableSeats"
            type="number"
            min="1"
            required
          />
        </div>

        <label for="price">Цена</label>
        <div>
          <input
            id="price"
            v-model.number="formData.price"
            type="number"
            min="0"
            step="0.01"
            required
          />
        </div>

        <div class="form-footer">
          <button type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? "Сохранение..." : "Создать маршрут" }}
          </button>
          <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </div>
          <div v-if="successMessage" class="success-message">
            {{ successMessage }}
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { adminService } from "@/shared/services/adminService";
import { formatToDatabase } from "@/shared/utils/formatTime";

const formData = ref({
  routeFrom: "",
  routeTo: "",
  date: "",
  departureTime: "",
  arrivalTime: "",
  transport: "",
  availableSeats: 1,
  price: 0,
});

const isSubmitting = ref(false);
const errorMessage = ref("");
const successMessage = ref("");
const updateDepartureTime = () => {
  const now = new Date().toISOString().slice(0, 16).replace(" ");
  if (formData.value.departureTime < now) {
    formData.value.departureTime = now;
  }
  formData.value.date = formData.value.departureTime.split("T")[0];
};

const updateArrivalTime = () => {
  if (formData.value.arrivalTime < formData.value.departureTime) {
    formData.value.arrivalTime = formData.value.departureTime;
  }
};

const clearError = () => {
  errorMessage.value = "";
  successMessage.value = "";
};

const resetForm = () => {
  formData.value = {
    routeFrom: "",
    routeTo: "",
    departureTime: "",
    arrivalTime: "",
    transport: "",
    availableSeats: 1,
    price: 0,
  };
};

const formatDateTime = (datetime) => {
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

    successMessage.value = "Маршрут успешно создан!";
    resetForm();
    setTimeout(() => (successMessage.value = ""), 3000);
  } catch (error) {
    errorMessage.value =
      error.response?.data?.message || "Ошибка при создании маршрута";
  } finally {
    isSubmitting.value = false;
  }
};
</script>
<script>
export default {};
</script>
<style scoped lang="scss">
$shadow: 0 0 8px rgba(0, 0, 0, 0.2);

.add-route-form {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;

  .input-form {
    box-shadow: $shadow;
    border-radius: 16px;
    padding: 2rem;
    max-width: 1200px;
    width: 100%;
    background: white;
    display: flex;
    flex-direction: column;
    gap: 1rem;

    select {
      background-position: right 1rem center;
      width: 100%;
      border: 1px solid #ddd;
      padding: 0.8rem 2.5rem;
      border-radius: 8px;
      cursor: pointer;
      background: white;
      color: black;
      border: 1.5px solid #ddd;
      transition: 0.3s all;

      &:hover {
        border-color: #447cdc;
        box-shadow: 0 0 12px rgba(68, 124, 220, 0.3);
      }

      &:focus {
        outline: none;
        border-color: #447cdc;
        box-shadow: 0 0 15px rgba(68, 124, 220, 0.4);
        background: white;
      }
    }

    .form-footer {
      margin-top: 1.5rem;
      display: flex;
      flex-direction: column;
      gap: 1rem;

      button {
        background: #447cdc;
        color: white;
        border: none;
        padding: 1rem;
        border-radius: 8px;
        font-size: 1.1rem;
        cursor: pointer;
        transition: all 0.3s ease;
        width: 100%;

        &:hover {
          background: #3668b8;
          box-shadow: 0 4px 15px rgba(68, 124, 220, 0.4);
        }

        &:disabled {
          background: #ddd;
          cursor: not-allowed;
        }
      }

      .error-message {
        color: #dc3545;
        font-size: 0.9rem;
        text-align: center;
      }
    }

    label {
      font-size: 1.2rem;
      color: #333;
      font-weight: 500;
    }

    input {
      border: 1.5px solid #ddd;
      border-radius: 8px;
      width: 100%;
      padding: 0.8rem 1.2rem;
      font-size: 1rem;
      transition: all 0.3s ease;
      background: white;
      color: #333;

      &:hover {
        border-color: #447cdc;
        box-shadow: 0 0 12px rgba(68, 124, 220, 0.3);
      }

      &:focus {
        outline: none;
        border-color: #447cdc;
        box-shadow: 0 0 15px rgba(68, 124, 220, 0.4);
        background: white;
      }

      &[type="date"],
      &[type="datetime-local"] {
        position: relative;
        padding-right: 2.5rem;
        appearance: none;
        cursor: pointer;

        &::-webkit-calendar-picker-indicator {
          opacity: 1;
          position: absolute;
          right: 0.5rem;
          width: 1.5rem;
          height: 1.5rem;
          padding: 0.2rem;
          background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23447cdc"><path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V9h14v10zM5 7V5h14v2H5z"/></svg>')
            no-repeat;
          background-size: contain;
          cursor: pointer;
          filter: brightness(0.8);
          transition: filter 0.2s ease;
        }

        &:hover::-webkit-calendar-picker-indicator {
          filter: brightness(1);
        }
      }
    }
  }
}
</style>
