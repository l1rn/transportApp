<script setup>
import { onMounted, ref } from 'vue';
import { useRoutesStore } from '@/stores/routesStore';

const store = useRoutesStore();
const searchInput = ref('');

onMounted(() => {
  store.fetchRoutes();
});
</script>

<template>
  <div class="container">
    <h1>Список маршрутов</h1>

    <input 
      v-model="searchInput"
      @input="store.setSearchQuery(searchInput)"
      placeholder="Поиск маршрута..."
      class="search-box"
    />

    <p v-if="store.loading">Загрузка...</p>
    <p v-if="store.error" class="error">{{ store.error }}</p>

    <ul v-if="!store.loading && !store.error && store.filteredRoutes.length">
      <li v-for="route in store.filteredRoutes" :key="route.id">
        <strong>{{ route.routeFrom }}</strong> → <strong>{{ route.routeTo }}</strong> ({{ route.date }})
      </li>
    </ul>

    <p v-if="!store.loading && store.filteredRoutes.length === 0">Маршрутов не найдено.</p>
  </div>
</template>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
}

.search-box {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.error {
  color: red;
}
</style>
