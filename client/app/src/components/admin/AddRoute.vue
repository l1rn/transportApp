<template>
    <div>
        <div class="add-route-form">
            <form class="input-form" @submit.prevent="handleSubmit">
                <h1>Создание маршрутов</h1>
                <label for="routeFrom">Откуда</label>
                <div>
                    <input
                    v-model="formData.routeFrom" 
                    id="routeFrom"
                    required
                    @input="clearError"
                    />
                </div>

                <label for="routeTo">Куда</label>
                <div>
                    <input 
                    v-model="formData.routeTo" 
                    id="routeTo"
                    required
                    @input="clearError"
                    />
                </div>

                <label for="date">Дата маршрута</label>
                <div>
                    <input 
                    type="date"
                    id="date"
                    v-model="formData.date"
                    :min="minDate"
                    required
                    @change="resetTimes"
                    />
                </div>

                <label for="departureTime">Время отправления</label>
                <div>
                    <input 
                    type="time" 
                    id="departureTime"
                    v-model="formData.departureTime" 
                    required
                    @change="updateArrivalTime"
                    />
                </div>

                <label for="arrivalTime">Время прибытия</label>
                <div>
                    <input 
                    type="time" 
                    id="arrivalTime"
                    v-model="formData.arrivalTime"
                    required
                    :min="formData.departureTime"
                    />
                </div>

                <label for="transport">Транспорт</label>
                <div>                    
                    <select 
                    id="transport" 
                    v-model="formData.transport"
                    required
                    >
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
                    <button 
                    type="submit" 
                    :disabled="isSubmitting"
                    >
                        {{ isSubmitting ? 'Сохранение...' : 'Создать маршрут' }}
                    </button>
                    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
                    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import AdminService from '@/services/AdminService'

const formData = ref({
    routeFrom: '',
    routeTo: '',
    date: '',
    departureTime: '',
    arrivalTime: '',
    transport: '',
    availableSeats: 1,
    price: 0
})

const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const minDate = computed(() => new Date().toISOString().split('T')[0])

const updateArrivalTime = () => {
    if (formData.value.arrivalTime < formData.value.departureTime) {
        formData.value.arrivalTime = formData.value.departureTime
    }
}

const resetTimes = () => {
    if (!formData.value.date) {
        formData.value.departureTime = ''
        formData.value.arrivalTime = ''
    }
}

const formatDateTime = (date, time) => {
    return new Date(`${date}T${time}`).toISOString()
}

const clearError = () => {
    errorMessage.value = ''
    successMessage.value = ''
}

const resetForm = () => {
    formData.value = {
        routeFrom: '',
        routeTo: '',
        date: '',
        departureTime: '',
        arrivalTime: '',
        transport: '',
        availableSeats: 1,
        price: 0
    }
}

const handleSubmit = async () => {
    try {
        isSubmitting.value = true
        clearError()

        const payload = {
            ...formData.value,
            date: new Date(formData.value.date).toISOString(),
            time: formatDateTime(formData.value.date, formData.value.departureTime),
            arrivalTime: formatDateTime(formData.value.date, formData.value.arrivalTime)
        }

        await AdminService.addRoute(payload)
        
        successMessage.value = 'Маршрут успешно создан!'
        resetForm()
        setTimeout(() => successMessage.value = '', 3000)

    } catch (error) {
        errorMessage.value = error.response?.data?.message || 'Ошибка при создании маршрута'
    } finally {
        isSubmitting.value = false
    }
}
</script>
<script>
export default {
    
}
</script>
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/add-route-form.sass"
</style>