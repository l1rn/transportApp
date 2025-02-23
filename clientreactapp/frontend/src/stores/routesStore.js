import { defineStore } from 'pinia';
import api from '@/services/api';

export const useRoutesStore = defineStore('routes', {
  state: () => ({
    routes: [],
    searchQuery: '',
    loading: false,
    error: null,
  }),
  getters:{
    filteredRoutes(state){
        if(!state.searchQuery) return state.routes;
        return state.routes.filter(route =>
            route.routeFrom.toLowerCase().includes(state.searchQuery.toLowerCase()) ||
            route.routeTo.toLowerCase().includes(state.searchQuery.toLowerCase)
        );
    }
  },

  actions: {
    async fetchRoutes() {
      this.loading = true;
      this.error = null;
      try {
        const response = await api.getRoutes();
        this.routes = response.data;
      } catch (err) {
        this.error = 'Ошибка загрузки маршрутов';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    setSearchQuery(query){
        this.searchQuery = query;
    }
  },
});
