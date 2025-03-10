<template>
        <div class="header-routes">
                <button
                @click="this.$router.replace('/home')" 
                class="button-back-header">Назад
                </button>
                <div class="custom-header-title">
                    <h1>Все маршруты</h1>
                </div>
                <div class="custom-subheader">
              
                </div>

            <div class="header-routes-title">
                <table class="table">
                    <thead>
                        <tr>
                        <th>ID</th>
                        <th>Откуда</th>
                        <th>Куда</th>
                        <th>Время отправления</th>
                        <th>Время прибытия</th>
                        <th>Дата</th>
                        <th>Транспорт</th>
                        <th>Доступно мест</th>
                        <th>Цена</th>
                        </tr>
                    </thead>
                    <tbody v-for="route in paginatedRoutes" :key="route.id">
                        <tr>
                        <td>{{ route.id }}</td>
                        <td>{{ route.routeFrom }}</td>
                        <td>{{ route.routeTo }}</td>
                        <td class="time-cell">
                            {{ formatTime(route.time) }}
                            <span class="date-sub">({{ formatDate(getDateSource(route)) }})</span>
                        </td>
                        <td class="time-cell">
                            {{ formatTime(route.arrivalTime) }}
                            <span class="date-sub">({{ formatDate(getDateSource(route, true)) }})</span>
                        </td>
                        <td>{{ route.date }}</td>
                        <td>{{ route.transport }} {{ checkRoutesEmoji(route.transport) }}</td>
                        <td>{{ route.availableSeats }}</td>
                        <td>{{ route.price }} р.</td>
                        </tr>
                    </tbody>
                </table>
                <div class="pagination">
                  <button
                    @click="currentPage--"
                    :disabled="currentPage === 1"
                    class="pagination-button"
                    >
                    Предыдущая
                  </button>
                  <span class="pagination-info">Страница {{ currentPage }} из {{ totalPages }}</span>
                  <button 
                          @click="currentPage++" 
                          :disabled="currentPage === totalPages"
                          class="pagination-button"
                      >
                          Следующая
                  </button>
                </div>
            </div>
        </div>
</template>
<script setup>
import RoutesService from '@/services/RoutesService';
import { ref, computed, onMounted } from 'vue';

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

</script>
<script>
export default {
    name: "AppRoutesFilter"
}
</script>
<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-filter-page"
</style>