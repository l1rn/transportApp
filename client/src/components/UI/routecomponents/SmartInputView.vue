<script setup>
import {ref, defineEmits, onMounted } from "vue";
import InputView from "../usercomponents/InputView.vue";
import RoutesService from "@/services/RoutesService";

const emit = defineEmits(['search-results']);
const routes = ref([]);
const searchResults = ref([])

let inputRouteFrom = ref('');
let inputRouteTo = ref('');

const selectedDate = ref(null);

const isLoading = ref(false);
const error = ref(null);

const selectedTransport = ref(null)
const selectedEmoji = ref(null)

onMounted(async() =>{
  await fetchRoutes();
})


const fetchRoutes = async () => {
  try {
    isLoading.value = true;
    const response = await RoutesService.getRoutes();
    routes.value = response.data;
  }catch (err) {
    error.value = 'Не удалось загрузить маршруты';
    console.log(err);
  }finally {
    isLoading.value = false;
  }
};
const searchRoutes = async () => {
    try{
        isLoading.value = true;
        const response = await RoutesService.searchRoutes(
        inputRouteFrom.value,
        inputRouteTo.value,
        selectedDate.value,
        selectedTransport.value,
      );
      searchResults.value = response.data;
      emit('search-results', searchResults.value.content);
    }
    catch(err){
      error.value = 'Ошибка при поиске маршрутов';
      console.error(err);
    }
    finally{
      isLoading.value = false
  }
}

const clearFilter = () => {
  inputRouteFrom.value = ''
  inputRouteTo.value = ''
  selectedDate.value = ''
  selectedEmoji.value = ''
  selectedTransport.value = ''
}

</script>
<template>
  <div class="">
    <div class="main-search-container">
      <div class="input-wrapper">
        <input-view />
        <input-view />
      </div>
    
      <div class="input-wrapper">
        <div
          class="date-input-wrapper"
          :class="{ 'has-value': selectedDate}"
        >
          <input
            id="date-input"
            v-model="selectedDate"
            type="date"
            @change="updatePlaceholder"
            @input="updatePlaceholder"
          >
        </div>
      </div>
      <div class="search-wrapper">
        <div class="sub-search-wrapper">
          <button 
            class="search-button-custom"
            :class="{'opacity-50': isLoading}"
            :disabled="isLoading"
            @click="searchRoutes"
          >
            <span v-if="!isLoading">Поиск</span>
            <span v-else>⌛</span>
            <span class="search-icon">🔍</span>
          </button>
          <button
            class="clear-wrapper"
            @click="clearFilter"
          >
            <span
              class="custom-clear-button"
            >✕</span>
          </button>
        </div>
        <div class="sub-search-container">
          <label>Транспорт: {{ selectedTransport }}{{ selectedEmoji }}</label>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/objects/smart-input"
</style>