<template>
  <div class="grid-wrapper">
    <div class="header-container">
      <h1>Поиск маршрутов по табло</h1>
    <SearchPageSearchBoxView 
    @search="handleSearch"
    @reset-page="handleResetPage"/>
    </div>
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">
          ID
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/dest.svg" alt="">
          Откуда
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/arrival.svg" alt="">
          Куда
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/transport.svg" alt="">
          Транспорт
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/clock.svg" alt="">
          Время вылета
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/clock.svg" alt="">
          Время прилета
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/seats.svg" alt="">
          Мест осталось
        </div>
        <div class="grid-cell">
          <img src="../../assets/icons/route/price.svg" alt="">
          Цена
        </div>
      </div>
      <div class="grid-content">
        <template
        v-for="route in routeData?.content"
        :key="route.id">
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
              <div class="grid-cell">
                {{ route.transport }}
              </div>
              <div class="grid-cell">
                {{ route.time }}
              </div>
              <div class="grid-cell">
                {{ route.arrivalTime }}
              </div>
              <div class="grid-cell">
                {{ route.availableSeats }}
              </div>
              <div class="grid-cell">
                {{ route.price }} Р.
              </div>
            </div>
        </template>
      </div>
    </div>
    <div class="button-container">
      <button 
      @click="handleBackward"
      :disabled="page <= 0">Назад</button>
      <span class="page-container">
        Страниц {{ page + 1 }} из {{ routeData?.totalPages || 1 }}
      </span>
      <button 
      :disabled="page >= routeData?.totalPages! - 1"
      @click="handleForward">Вперед</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { routesService } from "@/shared/services/routeService";
import { PaginatedRoute, RouteFilter } from "@/shared/types/route";
import { onMounted, ref } from 'vue';
import SearchPageSearchBoxView from "../molecule/search-page/SearchPageSearchBoxView.vue";

const routeData = ref<PaginatedRoute | null>(null);
const page = ref(0);
const currentFilter = ref<RouteFilter>({});

const handleForward = () => {
  if(!routeData.value) return;
  if(page.value >= routeData.value?.totalPages - 1){
    return;
  }
  page.value++;
  loadRoutesByFilter(currentFilter.value);
}

const handleBackward = () => {
  if(page.value <= 0) return;
  page.value--;
  loadRoutesByFilter(currentFilter.value);
}

const loadRoutesByFilter = async(
  filter: RouteFilter = {},
  resetPage = false
) => {
  if(resetPage){
    page.value = 0; 
  }
  const response = await routesService.searchRoutes(filter, page.value, 15);
  routeData.value = response.data;  
}

onMounted(() => {
  loadRoutesByFilter();  
})

const handleResetPage = () => {
  page.value = 0;
}

const handleSearch = async(filter: RouteFilter, page?: number) => {
  const isSameFilter = JSON.stringify(filter) === JSON.stringify(currentFilter.value);
  currentFilter.value = { ...filter };
  await loadRoutesByFilter(filter, !isSameFilter); 
}
</script>

<style scoped lang="scss">
@use "../../assets/styles/static/color" as colors;
@use "../../assets/styles/static/mixin" as mixins;

$grid-columns: 0.5fr 2fr 2fr 1.25fr 1.5fr 1.5fr 1.5fr 1fr;
img {
  width: 24px;
  height: 24px;
}

.grid-wrapper{
  @include mixins.display-column($jc: center, $ai: center);
  width: 100%;
  gap: 1rem;
  .header-container {
    width: 90%;
  }

  .button-container{
    @include mixins.display-center();
    gap: 2.5rem;
    margin-bottom: 1rem;
    button{
      @include mixins.button-clear(colors.$accent, white);
      padding: 1rem 2rem;
      font-size: 16px;
      font-weight: 600;
      border-radius: 16px;
      transition: all 0.3s ease;
      &:hover{
        background: colors.$primary-blue;
      }
      &:disabled{
        background: #6c757d;
        cursor: not-allowed;
      }
    }
  }
}

.grid-table{
  width: 90%;
  .grid-header .grid-cell{
    background: #DBEAFE;
    border-bottom: 2px solid #E2E8F0;
    &:first-child {
      border-radius: 16px 0 0 0;
    }
    &:last-child{
      border-radius: 0 16px 0 0;
    }
  }

  .grid-header, .grid-row{
    display: grid;
    grid-template-columns: $grid-columns;
  }

  .grid-row {
    background: #E2E8F0;
    transition: 
      background 0.3s ease, 
      transform 0.3s ease,
      border-radius 0.3s ease;
    &:nth-child(even){
      background: #F1F5F9;
    }
    &:hover{
      background: #E0E7FF;
      transform: scale(1.01);
      border-radius: 16px;
    }
    &:last-child{
      border-radius: 0 0 16px 16px;
    }
  }

  .grid-cell{
    @include mixins.display-center();
    text-align: center;
    padding: 1rem 0.5rem;
    gap: 0.25rem;
  }
}
</style>