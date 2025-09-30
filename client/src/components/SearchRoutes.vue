<template>
  <div class="all-items">
    <div class="header">
      <BackbuttonToHome />
      <h1>Поиск всех маршрутов</h1>
    </div>
    <div class="search-container">
      <div class="main-container">
        <div class="input-wrapper">
          <div class="input-group">
            <input v-model="inputRouteFrom" type="text" class="b-form-input" placeholder="Откуда"
              @focus="showFromSuggestions = true" @blur="handleFromBlur">
            <div v-if="showFromSuggestions">
              <div v-if="isLoading" class="loading">
                Загрузка...
              </div>
              <div v-else-if="error" class="error">
                {{ error }}
              </div>
              <div v-else class="suggestions">
                <div v-for="from in filteredFroms" :key="from" class="suggestion-item" @click="selectFrom(from)">
                  {{ from }}
                </div>
              </div>
            </div>
          </div>
          <div class="input-group">
            <input ref="toInput" v-model="inputRouteTo" class="b-form-input" placeholder="Куда" type="text"
              :disabled="!inputRouteFrom" @focus="handleToFocus" @blur="handleToBlur">
            <div v-if="showToSuggestions">
              <div v-if="isLoading" class="loading">
                Загрузка...
              </div>
              <div v-else-if="error" class="error">
                {{ error }}
              </div>
              <div v-else class="suggestions">
                <div v-for="to in filteredTos" :key="to" class="suggestion-item" @click="selectTo(to)">
                  {{ to }}
                </div>
              </div>
            </div>
          </div>

        </div>
        <div class="input-wrapper">
          <div class="date-input-wrapper" :class="{ 'has-value': selectedDate }">
            <input id="date-input" v-model="selectedDate" type="date" style="background-color: #f8fafc">
            <span class="custom-placeholder">Когда</span>
          </div>

          <div class="transport-wrapper">
            <div class="transport-toggle" :class="{ 'active': isOpen }" @click="toggleMenu">
              <span class="current-transport">
                Транспорт
              </span>
              <span class="arrow">▼</span>
            </div>

            <transition name="slide-fade">
              <div v-if="isOpen" ref="menu" class="transport-menu">
                <div v-for="transport in transports" :key="transport.value" class="transport-item"
                  @click="selectTransport(transport.value)">
                  <span class="emoji">{{ transport.emoji }}</span>
                  {{ transport.label }}
                </div>
              </div>
            </transition>
          </div>
        </div>

        <div class="search-wrapper">
          <button @click="clearFilter" class="clear-wrapper">
            <span class="custom-clear-button">✕</span>
          </button>
          <button class="search-button-custom btn" :class="{ 'opacity-50': isLoading }" :disabled="isLoading"
            @click="paginatedRoutes">
            <span v-if="!isLoading">Поиск</span>
            <span v-else>⌛</span>
            <span class="search-icon">🔍</span>
          </button>
        </div>
      </div>
      <div class="sub-search-container">
        <label class="transport-choosen-label">Транспорт: {{ selectedTransport }}{{ selectedEmoji }}</label>
      </div>
    </div>

    <div class="table-container">
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
        <tbody>
          <tr v-for="route in searchResults.content" :key="route.id">
            <td>{{ route.id }}</td>
            <td>{{ route.routeFrom }}</td>
            <td>{{ route.routeTo }}</td>
            <td class="time-cell">
              {{ formatTime(route.time) }}
              <span class="date-sub">({{ formatDate(getDateSource(route)) }})</span>
            </td>
            <td class="time-cell">
              {{ formatTime(route.arrivalTime) }}
              <span class="date-sub">({{ formatDate(getDateSource(route, true)) }})</span>
            </td>
            <td>{{ route.date }}</td>
            <td>{{ route.transport }} {{ searchEmoji(route.transport) }}</td>
            <td>{{ route.availableSeats }}</td>
            <td>{{ route.price }} р.</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button :disabled="currentPage === 0" class="page-button" @click="currentPage = currentPage - 1">
        Назад
      </button>

      <span class="page-info">
        Страница {{ currentPage + 1 }} из {{ totalPages }}
      </span>

      <button :disabled="currentPage + 1 >= totalPages" class="page-button" @click="currentPage = currentPage + 1">
        Вперед
      </button>
    </div>
  </div>
</template>
<script setup>
import BackbuttonToHome from './UI/BackbuttonToHome.vue';
import RoutesService from '@/services/RoutesService';
import { onBeforeMount, onMounted, reactive, ref, computed, nextTick, watch } from 'vue';

let inputRouteFrom = ref('');
let inputRouteTo = ref('');
const selectedDate = ref(null);

const routes = ref([]);
let searchResults = ref([])

const isLoading = ref(false);
const toInput = ref(null)
const error = ref(null)

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
  if (!inputRouteFrom.value) return [];
  const searchFrom = inputRouteFrom.value.toLowerCase();

  const fromRoutes = routes.value.filter(r =>
    r?.routeFrom?.toLowerCase() === searchFrom
  );
  return [...new Set(fromRoutes.map(r => r.routeTo))].filter(Boolean);
})

const filteredTos = computed(() => {
  if (!inputRouteTo.value) return availableTos.value;
  const search = inputRouteTo.value.toLowerCase();

  return availableTos.value.filter(to =>
    to?.toLowerCase().includes(search)
  );
});

const selectFrom = (from) => {
  inputRouteFrom.value = from
  showFromSuggestions.value = false
  nextTick(() => {
    if (toInput.value) {
      toInput.value.focus();
    }
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

const transports = reactive([
  { value: 'train', label: 'Поезд', emoji: '🚂' },
  { value: 'bus', label: 'Автобус', emoji: '🚌' },
  { value: 'air', label: 'Авиа', emoji: '✈️' }
])
const isOpen = ref(false)
const toggleMenu = () => {
  isOpen.value = !isOpen.value
}

const selectedTransport = ref(null)
const selectedEmoji = ref(null)

const selectTransport = (value) => {
  const transport = transports.find(t => t.value === value)
  selectedTransport.value = transport?.label || null
  selectedEmoji.value = transport?.emoji || null;
  isOpen.value = false
}
const clearFilter = () => {
  inputRouteFrom.value = ''
  inputRouteTo.value = ''
  selectedDate.value = ''
  selectedEmoji.value = ''
  selectedTransport.value = ''
}
onMounted(async () => {
  await fetchRoutes();
  await paginatedRoutes()
})


const fetchRoutes = async () => {
  try {
    isLoading.value = true;
    const response = await RoutesService.getRoutes();
    routes.value = response.data;
  } catch (err) {
    error.value = 'Не удалось загрузить маршруты';
    console.log(err);
  } finally {
    isLoading.value = false;
  }
};

const menuRef = ref(null);

const handleClickOutside = (event) => {
  if (menuRef.value && !menuRef.value.contains(event.target)) {
    isOpen.value = false;
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
onBeforeMount(() => {
  document.removeEventListener('click', handleClickOutside)
})
const currentPage = ref(0)
const totalPages = ref(0)
watch(currentPage, (newVal) => {
  paginatedRoutes(newVal)
})
const paginatedRoutes = async () => {
  try {
    isLoading.value = true;
    const response = await RoutesService.searchRoutes(
      inputRouteFrom.value,
      inputRouteTo.value,
      selectedDate.value,
      selectedTransport.value,
      currentPage.value,
      10
    )
    if (response.data && response.data.content) {
      searchResults.value = response.data
      totalPages.value = response.data.totalPages || 0
    }
    if (currentPage.value >= totalPages.value) {
      currentPage.value = Math.max(totalPages.value - 1, 0)
    }

  } catch (error) {
    console.error(error)
    error.value = 'Ошибка загрузки данных'
    searchResults.value = { content: [] }
    totalPages.value = 0
  } finally {
    isLoading.value = false;
  }
}

const searchEmoji = (transport) => {
  switch (transport) {
    case 'Поезд': return '🚂'
    case 'Авиа': return '✈️'
    case 'Автобус': return '🚌'
    default: return ''
  }
}

const getDateSource = (route, isArrival = false) => {
  const timeString = isArrival ? route.arrivalTime : route.time;

  if (timeString.includes(' ')) {
    return timeString.split(' ')[0];
  }
  return route.date;
};

const formatDate = (dateString) => {
  try {
    const parts = dateString.split('-');
    return `${parts[2]}-${parts[1]}`;
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
</script>
<style scoped lang="sass">
@import "@/assets/styles/searchObjects/search-box"
</style>