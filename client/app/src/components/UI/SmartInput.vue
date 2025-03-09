<script setup>
import {ref, reactive, defineEmits, onMounted, computed, nextTick} from "vue";
import RoutesService from "@/services/RoutesService";

const routes = ref([]);
let inputRouteFrom = ref('');
let inputRouteTo = ref('');

const selectedDate = ref(null);
const arrivalDate = ref(null);

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

const emit = defineEmits(['select'])
const isOpen = ref(false)
const selectedTransport = ref(null)
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
  selectedTransport.value = transport?.label || null
  isOpen.value = false
  emit('transport-selected', transport)
}

onMounted(async() =>{
  await fetchRoutes();
})

const hasValue = ref(false);

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
}
const updatePlaceholder = () => {
  hasValue.value = !!selectedDate.value;
}
</script>
<template>
  <div class="main-container">
    <div class="input-group">
      <input
          type="text"
          v-model="inputRouteFrom"
          class="b-form-input"
          @focus="showFromSuggestions = true"
          @blur="handleFromBlur"
          placeholder="Откуда"
      />
      <div v-if="showFromSuggestions">
        <div v-if="isLoading" class="loading">Загрузка...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <div v-else class="suggestions">
          <div
              v-for="from in filteredFroms"
              :key="from"
              class="suggestion-item"
              @click="selectFrom(from)">
            {{ from }}
          </div>
        </div>
      </div>
    </div>

    <div class="input-group">
      <input
          class="b-form-input ms-1"
          placeholder="Куда"
          v-model="inputRouteTo"
          type="text"
          :disabled="!inputRouteFrom"
          @focus="handleToFocus"
          @blur="handleToBlur"
          ref="toInput"
      />
      <div v-if="showToSuggestions">
        <div v-if="isLoading" class="loading">Загрузка...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <div v-else class="suggestions">
          <div class="suggestion-item"
               v-for="to in filteredTos"
               :key="to"
               @click="selectTo(to)">
            {{to}}
          </div>
        </div>
      </div>
    </div>


    <div class="date-input-wrapper"
        :class="{ 'has-value': selectedDate}">
      <input
          type="date"
          v-model="selectedDate"
          id="date-input"
          @change="updatePlaceholder"
          @input="updatePlaceholder"
        style="background-color: #f8fafc"/>
      <span class="custom-placeholder">Когда</span>
    </div>

    <div class="date-input-wrapper">
      <input
          type="date"
          id="date-input"
          v-model="arrivalDate"
          @change="updatePlaceholder"
          @input="updatePlaceholder"
          />
      <span class="custom-placeholder">Обратно</span>
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
        <div v-if="isOpen" class="transport-menu">
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

    <button class="search-button-custom btn"
            :class="{'opacity-50': isLoading}"
            :disabled="isLoading">

      <span v-if="!loading">Поиск</span>
      <span v-else>⌛</span>
      <span class="search-icon">🔍</span>
      
    </button>
    <button class="custom-clear-button" @click="clearAllSearch">Очистить выбор</button>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/objects/smart-input"
</style>