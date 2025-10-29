<template>
  <div class="main-search-container">
    <div class="search-wrapper">
      <div class="input-wrapper">
        <input-suggestion-view 
        v-model="filter.routeFrom" 
        type="text"
        suggestion-type="from"
        placeholder="Откуда"
        />
        <input-suggestion-view 
        v-model="filter.routeTo" 
        suggestion-type="to"
        type="text"
        placeholder="Куда"
        />
      </div>

      <div class="input-wrapper">
        <input-suggestion-view 
        v-model="filter.date" 
        type="date" />
        <input-suggestion-view 
        v-model="filter.transport" 
        type="select"
        placeholder="Транспорт"/>
      </div>
      <div class="search-container">
        <button 
        @click="searchRoutesByFilter"
        class="search-button-custom">
          Поиск
          <span class="search-icon">
            🔍
          </span>
        </button>
        <button 
        @click="clearSearchContainer"
        class="trash-button-custom">
          Очистить 
          <span class="trash-bin">
            🗑️
          </span>
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import InputSuggestionView from "@/components/atom/InputSuggestionView.vue";
import { RouteFilter } from "@/types/route";
import { routesService } from "@/services/routeService";
import { useRouteStore } from "@/stores/useRouteStore";

const routeStore = useRouteStore();

const filter = ref<RouteFilter>({
  routeFrom: "",
  routeTo: "",
  date: "",
  transport: ""
});

const transportTransform = (f: RouteFilter): RouteFilter => {
  const removeEmoji = (s?: string) => s?.replace(/^[^\p{L}\p{N}]+/u, '').trim();

  return {
    ...f,
    transport: removeEmoji(f?.transport) === "Любой" ? "" : removeEmoji(f?.transport)
  }
}

const searchRoutesByFilter = async() => {
  try{
    const routeFilter = transportTransform(filter.value)
    const response = await routesService.searchRoutes(routeFilter);
    routeStore.setRouteData(response.data);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
  catch(error){
    console.error(error);
  }
}

const clearSearchContainer = () => {
  filter.value = {
    routeFrom: "",
    routeTo: "",
    date: "",
    transport: ""
  };
}
</script>
<style scoped lang="sass">
@use "../../../assets/styles/molecule/home/search-container"
</style>