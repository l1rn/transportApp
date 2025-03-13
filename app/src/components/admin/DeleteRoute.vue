<template>
  <div class="routes-container">
    <div class="header">
      <h1>Все маршруты</h1>
    </div>
        
    <div class="table-wrapper">
      <table class="routes-table">
        <thead>
          <tr>
            <th
              v-for="header in headers"
              :key="header"
            >
              {{ header }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="route in paginatedRoutes"
            :key="route.id"
          >
            <td>{{ route.id }}</td>
            <td>{{ route.routeFrom }}</td>
            <td>{{ route.routeTo }}</td>
            <td class="time-cell">
              <div class="time-block">
                <span class="time">{{ formatTime(route.time) }}</span>
                <span class="date">{{ formatDate(getDateSource(route)) }}</span>
              </div>
            </td>
            <td class="time-cell">
              <div class="time-block">
                <span class="time">{{ formatTime(route.arrivalTime) }}</span>
                <span class="date">{{ formatDate(getDateSource(route, true)) }}</span>
              </div>
            </td>
            <td>{{ route.date }}</td>
            <td class="transport-cell">
              <span class="transport-icon">{{ checkRoutesEmoji(route.transport) }}</span>
              {{ route.transport }}
            </td>
            <td class="seats">
              {{ route.availableSeats }}
            </td>
            <td class="price">
              {{ route.price }} ₽
            </td>
            <td class="actions">
              <button 
                class="delete-btn"
                @click="deleteRoute(route.id)"
              >
                Удалить
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <button 
        :disabled="currentPage === 1" 
        class="pagination-btn prev"
        @click="currentPage--"
      >
        ← Назад
      </button>
      <span class="page-info">Страница {{ currentPage }} из {{ totalPages }}</span>
      <button 
        :disabled="currentPage === totalPages" 
        class="pagination-btn next"
        @click="currentPage++"
      >
        Вперед →
      </button>
    </div>
  </div>
</template>
<script setup>
import AdminService from '@/services/AdminService';
import RoutesService from '@/services/RoutesService';
import { ref, computed, onMounted } from 'vue';

const headers = ref([
    'ID', 'Откуда', 'Куда', 'Отправление', 
    'Прибытие', 'Дата', 'Транспорт', 'Мест', 'Цена', 'Действия'
]);

const routes = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(10);

const paginatedRoutes = computed(() =>{
const start = (currentPage.value - 1) * itemsPerPage.value;
const end = start + itemsPerPage.value;
return routes.value.slice(start,end);
})

const totalPages = computed(() => Math.ceil(routes.value.length / itemsPerPage.value))

const fetchRoutes = async() =>{
const response = await RoutesService.getRoutes();
routes.value = response.data;
}
onMounted(async() =>{
fetchRoutes();
})

const getDateSource = (route, isArrival = false) => {
const timeString = isArrival ? route.arrivalTime : route.time;

if(timeString.includes(' ')) {
return timeString.split(' ')[0];
}
return route.date;
};

const formatDate = (dateString) => {
try {
const [year, month, day] = dateString.split('-');
return `${day}-${month}`;
} catch {
return '??-??';
}
};

const formatTime = (timeString) => {
try {
const timePart = timeString.includes(' ') 
  ? timeString.split(' ')[1] 
  : timeString;
return timePart.slice(0, 5);
} catch {
return '--:--';
}
};

const checkRoutesEmoji = (transport) =>{
    switch(transport){
        case 'Поезд': return '🚂'
        case 'Авиа': return '✈️'
        case 'Автобус': return '🚌'
        default: return ''
    }
}

const deleteRoute = async(routeId) => {
    try{
        await AdminService.deleteRoute(routeId);
        await fetchRoutes();
    }catch(error){
        alert('Не удалось удалить маршрут!');
        console.log(error);
    }
}

</script>
<script>
export default {
name: "AppRoutesFilter"
}
</script>
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/delete-page"
</style>