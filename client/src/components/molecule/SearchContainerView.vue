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
        type="text"/>
        <input-suggestion-view 
        v-model="filter.transport" 
        type="text"/>
      </div>
      <div class="search-container">
        <button class="search-button-custom">
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
import InputSuggestionView from "../atom/InputSuggestionView.vue";

interface RouteFilter {
  routeFrom: string | null;
  routeTo: string | null;
  date: string | null;
  transport: string | null;
}

const filter = ref<RouteFilter>({
  routeFrom: "",
  routeTo: "",
  date: "",
  transport: ""
});

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
@import "@/assets/styles/objects/search-container"
</style>