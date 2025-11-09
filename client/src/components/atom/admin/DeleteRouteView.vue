<template>
  <div class="routes-container">
    <div class="header">
      <h1>Все маршруты</h1>
    </div>

    <div class="table-wrapper">
      <table class="routes-table">
        <thead>
          <tr>
            <th v-for="header in headers" :key="header">
              {{ header }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="route in paginatedRoutes" :key="route.id">
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
            <td class="">
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
              <button class="delete-btn" :disabled="deletingId === route.id" @click="deleteRoute(route.id)">
                <span v-if="deletingId === route.id">Удаление...</span>
                <span v-else>Удалить</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <button :disabled="currentPage === 1" class="pagination-btn prev" @click="currentPage--">
        ← Назад
      </button>
      <span class="page-info">Страница {{ currentPage }} из {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" class="pagination-btn next" @click="currentPage++">
        Вперед →
      </button>
    </div>
  </div>
</template>
<script setup>
import { adminService } from '@/shared/services/adminService';
import { routesService } from '@/shared/services/routeService';
import { ref, computed, onMounted } from 'vue';

const headers = ref([
  'ID', 'Откуда', 'Куда', 'Отправление',
  'Прибытие', 'Дата', 'Транспорт', 'Мест', 'Цена', 'Действия'
]);

const routes = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(10);

const paginatedRoutes = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return routes.value.slice(start, end);
})

const totalPages = computed(() => Math.ceil(routes.value.length / itemsPerPage.value))

const fetchRoutes = async () => {
  const response = await routesService.getRoutes();
  routes.value = response.data;
}
onMounted(async () => {
  fetchRoutes();
})

const getDateSource = (route, isArrival = false) => {
  const timeString = isArrival ? route.arrivalTime : route.time;

  if (timeString.includes(' ')) {
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

const checkRoutesEmoji = (transport) => {
  switch (transport) {
    case 'Поезд': return '🚂'
    case 'Авиа': return '✈️'
    case 'Автобус': return '🚌'
    default: return ''
  }
}

const notifications = ref(null)
const deletingId = ref(null);
const deletedRoute = ref(null);
const deleteRoute = async (routeId) => {
  let fullIndex;
  try {
    deletingId.value = routeId;

    fullIndex = routes.value.findIndex(r => r.id === routeId);
    if (fullIndex === -1) return;

    const hasBooking = await checkRouteForBooking(routeId);
    if (hasBooking) {
      notifications.value.showNotification('error', 'Нельзя удалить маршрут с активными бронированиями');
      return;
    }

    deletedRoute.value = routes.value[fullIndex];
    routes.value.splice(fullIndex, 1);

    await adminService.deleteRoute(routeId);
    notifications.value.showNotification('success', 'Маршрут успешно удален');

  } catch (error) {
    if (deletedRoute.value && fullIndex !== -1) {
      routes.value.splice(fullIndex, 0, deletedRoute.value);
    }
    notifications.value.showNotification(
      'error',
      error.response?.data?.message || 'Ошибка при удалении маршрута'
    );
  } finally {
    deletingId.value = null;
    deletedRoute.value = null;
  }
}

const checkRouteForBooking = async (routeId) => {
  try {
    const response = await adminService.getAllBookings()
    return response.data.some(booking => booking.route.id === routeId)
  } catch (error) {
    notifications.value.showNotification('error', 'Ошибка при проверке бронирований')
    return false
  }
}

</script>
<script>
export default {
  name: "AppRoutesFilter"
}
</script>
<style scoped lang="scss">
* {
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.routes-container {
  padding: 2rem;
  margin: 0 auto;
}

.header {
  margin-bottom: 2rem;

  h1 {
    font-size: 2rem;
    color: #2c3e50;
    border-bottom: 3px solid #3498db;
    padding-bottom: 0.5rem;
  }
}

.table-wrapper {
  overflow-x: auto;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}

.routes-table {
  width: 100%;
  border-collapse: collapse;
  background: white;

  th,
  td {
    padding: 1rem;
    text-align: left;
    border-bottom: 1px solid #ecf0f1;
  }

  th {
    background: #3498db;
    color: white;
    font-weight: 600;

    &:first-child {
      border-radius: 12px 0 0 0;
    }

    &:last-child {
      border-radius: 0 12px 0 0;
    }
  }

  tr:hover {
    background: #f8f9fa;
  }

  .time-block {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;

    .time {
      font-weight: 500;
      color: #2c3e50;
    }

    .date {
      font-size: 0.85rem;
      color: #7f8c8d;
    }
  }

  .seats {
    text-align: center;
  }

  .price {
    color: #27ae60;
    font-weight: 500;
  }

  .actions {
    text-align: right;
  }
}

.delete-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #c0392b;
  }

  .icon {
    width: 16px;
    height: 16px;
  }
}

.pagination {
  margin-top: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;

  .pagination-btn {
    padding: 0.5rem 1rem;
    border: 1px solid #3498db;
    border-radius: 6px;
    background: white;
    color: #3498db;
    cursor: pointer;
    transition: all 0.2s;

    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    &:hover:not(:disabled) {
      background: #3498db;
      color: white;
    }
  }

  .page-info {
    color: #7f8c8d;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .routes-container {
    padding: 1rem;
  }

  .routes-table {

    th,
    td {
      padding: 0.75rem;
      font-size: 0.9rem;
    }
  }
}
</style>