<template>
  <div class="routes-container">
    <ChangeRouteFormView
      v-if="isFormVisible"
      :route-data="selectedRoute!"
      @close="closeEditForm"
      @submit="handleFormSubmit"
    />
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">
          ID
        </div>
        <div class="grid-cell">
          Откуда
        </div>
        <div class="grid-cell">
          Куда
        </div>
        <div class="grid-cell">
          Отправление
        </div>
        <div class="grid-cell">
          Прибытие
        </div>
        <div class="grid-cell">
          Дата
        </div>
        <div class="grid-cell">
          Транспорт
        </div>
        <div class="grid-cell">
          Осталось мест
        </div>
        <div class="grid-cell">
          Цена
        </div>
        <div class="grid-cell">

        </div>
      </div>
      <div class="grid-content">
        <template v-for="route in routes?.content" :key="route.id">
          <div class="grid-row">
            <div class="grid-cell">
              {{ route.id }}
            </div>
            <div class="grid-cell">
              {{ route.routeFrom }}
            </div>
            <div class="grid-cell">
              {{ route.routeTo }}
            </div>
            <div class="grid-cell time-cell">
              <span class="time">{{ formatTime(route.destinationTime) }} </span>
              <span class="date">{{ formatDate(getDateSource(route)) }}</span>
            </div>
            <div class="grid-cell time-cell">
              <span class="time">{{ formatTime(route.arrivalTime) }} </span>
              <span class="date">{{ formatDate(getDateSource(route)) }}</span>
            </div>
            <div class="grid-cell">
              {{ route.date }}
            </div>
            <div class="grid-cell">
              <span class="transport-icon">{{ formatUlils.formatTransportStringToEmoji(route.transport) }}</span>
              {{ route.transport }}
            </div>
            <div class="grid-cell">
              {{ route.availableSeats }}
            </div>
            <div class="grid-cell">
              {{ route.price }} ₽
            </div>
            <div class="grid-cell">
              <div class="actions-container">
                <button @click="openEditForm(route)">
                  Изменить
                </button>
                <button @click="deleteRoute(route.id!)">
                  Удалить
                </button>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="pagination">
      <button :disabled="page <= 0" class="pagination-btn" @click="handleBackward">
        ← Назад
      </button>
      <span class="page-info">Страница {{ page + 1 }} из {{ routes?.totalPages }}</span>
      <button :disabled="page >= routes?.totalPages! - 1" class="pagination-btn" @click="handleForward">
        Вперед →
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
import ChangeRouteFormView from "@/components/atom/admin/ChangeRouteFormView.vue";
import notification from "@/shared/plugins/notifications";
import { adminService } from "@/shared/services/adminService";
import { routesService } from "@/shared/services/routesService";
import { PaginatedResponse } from "@/shared/types/response";
import { Route } from "@/shared/types/route";
import { useFormatUtils } from "@/shared/utils/formatUlils";
import { ref, onMounted } from "vue";

const isFormVisible = ref(false);
const selectedRoute = ref<Route | null>(null);

const openEditForm = (route: Route) => {
  selectedRoute.value = route;
  isFormVisible.value = true;
}

const closeEditForm = () => {
  isFormVisible.value = false;
  selectedRoute.value = null;
}

const handleFormSubmit = async() => {
  await routesService.updateRoute(selectedRoute.value?.id!, );
}

const formatUlils = useFormatUtils();
const page = ref(0);
const routes = ref<PaginatedResponse<Route>>();

const handleForward = () => {
  if (page.value >= routes.value?.totalPages! - 1) return;
  page.value++;
  loadRoutes(page.value);
};

const handleBackward = () => {
  if (page.value <= 0) return;
  page.value--;
  loadRoutes(page.value);
};

const loadRoutes = async (page: number = 0) => {
  const response = await routesService.searchRoutes({}, page);
  routes.value = response.data;
  console.log(routes.value);
};

onMounted(() => {
  loadRoutes();
});

const getDateSource = (route: Route, isArrival = false) => {
  const timeString = isArrival ? route.arrivalTime : route.destinationTime;

  if (timeString.includes(" ")) {
    return timeString.split(" ")[0];
  }
  return route.date;
};

const formatDate = (dateString: string) => {
  try {
    const [year, month, day] = dateString.split("-");
    return `${day}-${month}`;
  } catch {
    return "??-??";
  }
};

const formatTime = (timeString: string) => {
  try {
    const timePart = timeString.includes(" ")
      ? timeString.split(" ")[1]
      : timeString;
    return timePart.slice(0, 5);
  } catch {
    return "--:--";
  }
};

const deleteRoute = async (routeId: number) => {
  try {
    await adminService.deleteRoute(routeId);
    notification.success("Маршрут успешно удален");
  } catch (error) {
    notification.error("Ошибка при удалении маршрута");
  }
};
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

$grid-columns: 0.4fr 1.25fr 1.25fr 1fr 0.75fr 0.9fr 0.9fr 1fr 1fr 2fr;

.routes-container{
  @include mixins.display-column();
  gap: 1rem;
}
.grid-table {
  .grid-header {
    background: #3498db;
    color: white;
    font-weight: 600;
    border-radius: 16px 16px 0 0;
  }

  .grid-header,
  .grid-row {
    display: grid;
    grid-template-columns: $grid-columns;
    border-bottom: 1px solid #fbfeff;
  }

  .grid-row {
    background: #fbfffd;

    &:hover {
      background: #f8f9fa;
    }
    &:last-child{
      border-radius: 0 0 16px 16px
    }
  }

  .grid-cell {
    @include mixins.display-center();
    text-align: center;
    padding: 0.5rem;
    font-size: 14px;
    word-break: break-all;
    gap: 0.5rem;
    
    .actions-container {
      display: flex;
      gap: 0.5rem;

      button {
        padding: 0.5rem 1rem;
        border-radius: 16px;
        font-size: 14px;
        transition: all 0.3s ease;

        &:first-child {
          @include mixins.button-clear(white, #8b7373);
          border: 1px solid #8b7373;
          &:hover {
            background: #8b7373;
            color: white;
          }
        }

        &:last-child {
          @include mixins.button-clear(#e74c3c, white);
          cursor: pointer;

          &:hover {
            background: #c0392b;
          }
        }
      }
    }
  }

  .time-cell{
    @include mixins.display-column();
    .time {
      font-weight: 500;
      color: #2c3e50;
    }

    .date {
      font-size: 0.85rem;
      color: #7f8c8d;
    } 
  }
}

.pagination {
  margin-bottom: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;

  .pagination-btn {
    @include mixins.button-clear(#3498db, white);
    padding: 0.5rem 1rem;
    border: 1px solid #3498db;
    border-radius: 6px;
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
</style>
