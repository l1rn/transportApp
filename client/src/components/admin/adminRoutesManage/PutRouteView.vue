<template>
  <div>
    <div class="add-route-form">
      <form
        class="input-form"
        @submit.prevent="handleSubmit"
      >
        <h1>Изменение маршрутов</h1>
        <label for="routeId">ID у которого хотите поменять</label>
        <div>
          <div>
            <input
              id="routeId" 
              v-model="routeID"
              required
              min="1"
              @input="clearError"
            >
          </div>
        </div>
        <label for="routeFrom">Откуда</label>
        <div>
          <input
            id="routeFrom" 
            v-model="formData.routeFrom"
            required
            @input="clearError"
          >
        </div>

        <label for="routeTo">Куда</label>
        <div>
          <input 
            id="routeTo" 
            v-model="formData.routeTo"
            required
            @input="clearError"
          >
        </div>

        <label for="time">Время отправления</label>
        <div>
          <input 
            id="time"
            v-model="formData.time"
            type="datetime-local" 
            required
            @change="updateDepartureTime"
          >
        </div>

        <label for="arrivalTime">Время прибытия</label>
        <div>
          <input 
            id="arrivalTime"
            v-model="formData.arrivalTime"
            type="datetime-local"
            required
            :min="formData.time"
            @change="updateArrivalTime"
          >
        </div>

        <label for="transport">Транспорт</label>
        <div>                    
          <select 
            id="transport" 
            v-model="formData.transport"
            required
          >
            <option value="Автобус">
              Автобус
            </option>
            <option value="Поезд">
              Поезд
            </option>
            <option value="Авиа">
              Авиа
            </option>
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
          >
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
          >  
        </div>

        <div class="form-footer">
          <button 
            type="submit" 
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? 'Сохранение...' : 'Изменить' }}
          </button>
          <div
            v-if="errorMessage"
            class="error-message"
          >
            {{ errorMessage }}
          </div>
          <div
            v-if="successMessage"
            class="success-message"
          >
            {{ successMessage }}
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import AdminService from '@/services/AdminService'
import { formatToDatabase } from '@/services/dateFormat/formatTime';

const routeID = ref(1);
const formData = ref({
    routeFrom: '',
    routeTo: '',
    date: '',
    time: '',
    arrivalTime: '',
    transport: '',
    availableSeats: 1,
    price: 0
})

const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const updateDepartureTime = () =>{
    const now = new Date().toISOString().slice(0, 16) ;
    if(formData.value.time < now){
        formData.value.time = now;
    }
    formData.value.date = formData.value.time.split('T')[0]
}

const updateArrivalTime = () => {
    if (formData.value.arrivalTime < formData.value.time) {
        formData.value.arrivalTime = formData.value.time;
    }
}


const clearError = () => {
    errorMessage.value = ''
    successMessage.value = ''
}

// const resetForm = () => {
//     formData.value = {
//         routeFrom: '',
//         routeTo: '',
//         date: '',
//         departureTime: '',
//         arrivalTime: '',
//         transport: '',
//         availableSeats: 1,
//         price: 0
//     }
// }

const formatDateTime = (datetime) => {
    if(!datetime) return ''
    return formatToDatabase(datetime)
}

const handleSubmit = async () => {
    try {
        isSubmitting.value = true
        clearError()

        const payload = {
            ...formData.value,
            date: formData.value.date,
            time: formatDateTime(formData.value.time),
            arrivalTime: formatDateTime(formData.value.arrivalTime)
        }
        await AdminService.putRoute(routeID.value, payload)
        
        successMessage.value = 'Маршрут успешно изменен!'
        setTimeout(() => successMessage.value = '', 3000)

    } catch (error) {
        errorMessage.value = error.response?.data?.message || 'Ошибка при создании маршрута'
    } finally {
        isSubmitting.value = false
    }
}
</script>
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/add-route-form.sass"
</style>