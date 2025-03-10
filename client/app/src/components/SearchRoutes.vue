<template>
    <div class="all-items">
      <div class="header">
          <button @click="this.$router.push('/home')">Назад</button>
          <h1>Поиск всех маршрутов</h1>
      </div>
          <div class="search-container">
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
              <div v-if="isOpen" class="transport-menu" ref="menu">
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
  
          <button 
          class="search-button-custom btn"
          :class="{'opacity-50': isLoading}"
          :disabled="isLoading"
          @click="paginatedRoutes"
        >
          <span v-if="!isLoading">Поиск</span>
          <span v-else>⌛</span>
          <span class="search-icon">🔍</span>
        </button>
        </div>
        <div class="sub-search-container">
          <label class="transport-choosen-label"
          >Выбран транспорт: {{ selectedTransport }}{{selectedEmoji}}</label>
          <button class="custom-clear-button" @click="clearFilter">Очистить фильтр</button> 
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
    </div> 
  </template>
  <script setup>
  import RoutesService from '@/services/RoutesService';
  import { onBeforeMount, onMounted, reactive, ref, computed, nextTick } from 'vue';
  
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
  
  const menuRef = ref(null);
  
  const handleClickOutside = (event) => {
    if(menuRef.value && !menuRef.value.contains(event.target)){
      isOpen.value = false;
    }
  }
  
  onMounted(() => {
    document.addEventListener('click', handleClickOutside)
  })
  onBeforeMount(() =>{
    document.removeEventListener('click', handleClickOutside)
  })
  
  const paginatedRoutes = async() =>{
    try{
      isLoading.value = true;
      const response = await RoutesService.searchRoutes(
        inputRouteFrom.value,
        inputRouteTo.value,
        selectedDate.value,
        selectedTransport.value
      )
      searchResults.value = response.data;
    }catch(error){
      console.log(error);
    }finally{
      isLoading.value = false;
    }
  }

const searchEmoji = (transport) =>{
    switch(transport){
        case 'Поезд': return '🚂'
        case 'Авиа': return '✈️'
        case 'Автобус': return '🚌'
        default: return ''
    }
}

const getDateSource = (route, isArrival = false) => {
  const timeString = isArrival ? route.arrivalTime : route.time;
  
  if(timeString.includes(' ')) {
    return timeString.split(' ')[0];
  }
  return route.date;
};

const formatDate = (dateString) => {
  try {
    const [month, day] = dateString.split('-');
    return `${day}-${month}`;
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