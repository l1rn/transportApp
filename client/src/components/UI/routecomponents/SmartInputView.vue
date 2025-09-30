<script setup>
import {ref, reactive, defineEmits, onMounted, computed, nextTick} from "vue";
import RoutesService from "@/services/RoutesService";

const emit = defineEmits(['search-results']);
const routes = ref([]);
const searchResults = ref([])

let inputRouteFrom = ref('');
let inputRouteTo = ref('');

const selectedDate = ref(null);

const isLoading = ref(false);
const error = ref(null);
const toInput = ref(null)

const showFromSuggestions = ref(false);
const showToSuggestions = ref(false);

const uniqueFroms = computed(() => {
  return [...new Set(routes.value.map(route => route.routeFrom))];
})

const filteredFroms = computed(() => {
  if (!inputRouteFrom.value) return [];
  const search = inputRouteFrom.value.toLowerCase();

  return uniqueFroms.value.filter(route =>
      route?.toLowerCase().includes(search)
  );
});

const availableTos = computed(() => {
  if(!inputRouteFrom.value) return [];
  const searchFrom = inputRouteFrom.value.toLowerCase();

const fromRoutes = routes.value.filter(r =>
    r?.routeFrom?.toLowerCase() === searchFrom
  );
  return [...new Set(fromRoutes.map(r => r.routeTo))].filter(Boolean);
})

const filteredTos = computed(() => {
  if(!inputRouteTo.value) return availableTos.value;
  const search = inputRouteTo.value.toLowerCase();

  return availableTos.value.filter(to =>
    to?.toLowerCase().includes(search)
  );
});

const selectFrom = (from) =>{
  inputRouteFrom.value = from
  showFromSuggestions.value = false
  nextTick(() => {
    toInput.value.focus()
  })
};

const selectTo = (to) => {
  inputRouteTo.value = to;
  showFromSuggestions.value = false;
}

const handleToFocus = () => {
  showFromSuggestions.value = false;
  showToSuggestions.value = true;
}
const handleFromBlur = () => {
  setTimeout(() => {
    showFromSuggestions.value = false
  }, 150)
}

const handleToBlur = () => {
  setTimeout(() => {
    showToSuggestions.value = false
  }, 150)
}

const isOpen = ref(false)
const selectedTransport = ref(null)
const selectedEmoji = ref(null)
const transports = reactive([
  { value: 'train', label: 'Поезд', emoji: '🚂' },
  { value: 'bus', label: 'Автобус', emoji: '🚌' },
  { value: 'air', label: 'Авиа', emoji: '✈️' }
])

const toggleMenu = () => {
  isOpen.value = !isOpen.value
}

const selectTransport = (value) => {
  const transport = transports.find(t => t.value === value)
  selectedTransport.value = transport?.label || null;
  selectedEmoji.value = transport?.emoji || null;
  isOpen.value = false;
}

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
<div class="search-sidebar">
  <div class="main-container">
    <div class="input-wrapper">
      <div class="input-group">
      <input
        v-model="inputRouteFrom"
        type="text"
        class="b-form-input"
        placeholder="Откуда"
        @focus="showFromSuggestions = true"
        @blur="handleFromBlur"
      >
      <div v-if="showFromSuggestions">
        <div
          v-if="isLoading"
          class="loading"
        >
          Загрузка...
        </div>
        <div
          v-else-if="error"
          class="error"
        >
          {{ error }}
        </div>
        <div
          v-else
          class="suggestions"
        >
          <div
            v-for="from in filteredFroms"
            :key="from"
            class="suggestion-item"
            @mousedown.prevent="selectFrom(from)"
          >
            {{ from }}
          </div>
        </div>
      </div>
    </div>

      <div class="input-group">
        <input
          ref="toInput"
          v-model="inputRouteTo"
          class="b-form-input"
          placeholder="Куда"
          type="text"
          @focus="handleToFocus"
          @blur="handleToBlur"
        >
        <div v-if="showToSuggestions">
          <div
            v-if="isLoading"
            class="loading"
          >
            Загрузка...
          </div>
          <div
            v-else-if="error"
            class="error"
          >
            {{ error }}
          </div>
          <div
            v-else
            class="suggestions"
          >
             <div
              v-for="to in filteredTos"
              :key="to"
              class="suggestion-item"
              @mousedown.prevent="selectTo(to)"
            >
              {{ to }}
            </div>
          </div>
        </div>
      </div>
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
          style="background-color: #f8fafc"
          @change="updatePlaceholder"
          @input="updatePlaceholder"
        >
        <span class="custom-placeholder">Когда</span>
      </div>


    <div class="transport-wrapper">
      <div
        class="transport-toggle"
        :class="{ 'active': isOpen }"
        @click="toggleMenu"
      >
        <span class="current-transport">
          Транспорт
        </span>
        <span class="arrow">▼</span>
      </div>

        <transition name="slide-fade">
          <div
            v-if="isOpen"
            class="transport-menu"
          >
            <div
              v-for="transport in transports"
              :key="transport.value"
              class="transport-item"
              @click="selectTransport(transport.value)"
            >
              <span class="emoji">{{ transport.emoji }}</span>
              {{ transport.label }}
            </div>
          </div>
        </transition>
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
        @click="clearFilter"
        class="clear-wrapper">
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