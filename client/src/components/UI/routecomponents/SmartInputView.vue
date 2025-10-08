<script setup lang="ts">
import { ref } from "vue";
import InputView from "../usercomponents/InputView.vue";
interface RouteFilter {
  routeFrom: string | null;
  routeTo: string | null;
  date: string | null;
  transport: string | null;
}

const filter = ref<RouteFilter>({
  routeFrom: null,
  routeTo: null,
  date: null,
  transport: null
});


</script>
<template>
  <div class="main-search-container">
    <div class="search-wrapper">
      <div class="input-wrapper">
        <input-view 
        v-model="filter.routeFrom" 
        type="text" />
        <input-view 
        v-model="filter.routeTo" 
        type="text"/>
      </div>

      <div class="input-wrapper">
        <input-view 
        v-model="filter.date" 
        type="text"/>
        <input-view 
        v-model="filter.transport" 
        type="text"/>
      </div>
      <div class="search-wrapper">
        <button>Поиск</button>
        <button>X</button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/objects/smart-input"
</style>