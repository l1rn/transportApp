<template>
  <div class="header-routes">
    <div class="custom-header-title">
      <h1>Все маршруты</h1>
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
        <tbody
          v-for="route in routes?.content"
          :key="route.id"
        >
          <tr>
            <td>{{ route.id }}</td>
            <td>{{ route.routeFrom }}</td>
            <td>{{ route.routeTo }}</td>
            <td class="time-cell">
              <span class="time-sub">
                {{ route.destinationTime.split('T')[1] }}
              </span>
              <span class="date-sub">({{ route.destinationTime.split('T')[0] }})</span>
            </td>
            <td class="time-cell">
              <span class="time-sub">
                {{ route.arrivalTime.split('T')[1] }}
              </span>
              <span class="date-sub">({{ route.arrivalTime.split('T')[0] }})</span>
            </td>
            <td>{{ route.destinationTime  }}</td>
            <td>{{ route.transport }} {{ useFormatUtils().formatTransportStringToEmoji(route.transport) }}</td>
            <td>{{ route.availableSeats }}</td>
            <td>{{ route.price }} р.</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button
          :disabled="page <= 0"
          class="pagination-button"
          @click="handleBackward"
        >
          Предыдущая
        </button>
        <span class="pagination-info">
          Страница {{ page }} из {{ routes?.totalPages! - 1 }}
        </span>
        <button
          :disabled="page >= routes?.totalPages! - 1"
          class="pagination-button"
          @click="handleForward()"
        >
          Следующая
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { routesService } from '@/shared/services/routesService';
import { PaginatedResponse } from '@/shared/types/response';
import { Route } from '@/shared/types/route';
import { useFormatUtils } from '@/shared/utils/formatUlils';
import { ref, onMounted } from 'vue';

const routes = ref<PaginatedResponse<Route>>();
const page = ref(0);

const fetchRoutes = async () => {
  const response = await routesService.searchRoutes({ }, page.value, 10);
  routes.value = response.data;
}
onMounted(async () => {
  fetchRoutes();
})

const handleForward = () => {
  if(!routes.value) return;
  if(page.value >= routes.value?.totalPages - 1) return;
  page.value++;
  fetchRoutes();
}

const handleBackward = () => {
  if(!routes.value) return;
  if(page.value >= routes.value?.totalPages - 1) return;
  page.value--;
  fetchRoutes();
}
</script>
<style scoped lang="scss">
@use "../../assets/styles/static/_mixin" as mixins;

$primary-color: #007bff;
$secondary-color: #6c757d;
$hover-bg: rgba(0,123,255,0.05);
$transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

* {
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;

  box-sizing: border-box;
}
.header-routes {
  min-height: 100vh;
  padding: 0 6%;
  background: #fff;
  .custom-header-title {
    margin-bottom: 1.4rem;
  }
  .custom-subheader {
    border: 1px solid;
    margin-bottom: 2rem;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  .button-back {
    align-self: flex-start;
    padding: 1rem 2rem;
    background: $primary-color;
    color: white;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    transition: $transition;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }
}
.table-container {
  max-width: 1500px;
  margin: 2rem auto;
  overflow-x: auto;
  border-radius: 24px;
  background: white;
}
.table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 24px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 0 24px rgba(3, 7, 197, 0.08);
  transition: all 1.2s;

  &:hover {
    box-shadow: 0 0 27px rgba(3, 7, 197, 0.16);
  }
  thead {
    background: linear-gradient(135deg, $primary-color 0%, darken($primary-color, 8%) 100%);
    color: white;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;

    th {
      padding: 1.5rem 1.8rem;
      position: relative;

      &:not(:last-child)::after {
        content: '';
        position: absolute;
        right: 0;
        top: 50%;
        transform: translateY(-50%);
        height: 60%;
        width: 1px;
        background: rgba(255,255,255,0.2);
      }
    }
  }
  tbody {
    tr {
      transition: $transition;

      &:nth-child(even) {
        background: #f8f9fa;
      }
      &:hover {
        background: $hover-bg;
        transform: scale(1.005);
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
      }
      td {
        text-align: center;
        padding: 1.2rem 1.8rem;
        font-weight: 500;
        color: #2c3e50;
        border-bottom: 1px solid rgba(0,0,0,0.05);

        &:first-child {
          font-weight: 600;
          color: $primary-color;
        }
      }
    }
  }
}
.time-cell {
  position: relative;
  min-width: 140px;
  font-weight: 600 !important;
  .time-sub{
      margin-bottom: 0.5rem;
      &::before {
      content: '🕒';
      margin-right: 8px;
      filter: drop-shadow(0 2px 2px rgba(0,0,0,0.1));
    }
  }
  .date-sub {
    display: inline-block;
    padding: 2px 8px;
    background: rgba(0,0,0,0.05);
    border-radius: 6px;
    font-size: 0.75em;
    color: $secondary-color;
    margin-top: 0.25rem;
    margin-left: 8px;
    transform: translateY(-2px);
  }
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
  margin: 3rem 0 1rem;

  &-button {
    padding: 0.8rem 2rem;
    background: linear-gradient(135deg, $primary-color 0%, darken($primary-color, 5%) 100%);
    color: white;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    transition: $transition;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    font-weight: 600;

    &:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 6px 12px rgba(0,0,0,0.15);
    }
    &:disabled {
      background: #dee2e6;
      color: #adb5bd;
      cursor: not-allowed;
    }
  }
  &-info {
    font-size: 0.9em;
    color: $secondary-color;
    font-weight: 500;
  }
}
@media (max-width: 1200px) {
  .header-routes {
    padding: 2rem 5%;
  }
  .table {
    thead th,
    tbody td {
      padding: 1rem 1.2rem;
    }
  }
}
@media (max-width: 768px) {
  .table {
    min-width: 600px;

    thead th {
      font-size: 0.9em;
      padding: 1rem;
    }
    tbody td {
      font-size: 0.85em;
      padding: 0.8rem;
    }
  }
};
</style>