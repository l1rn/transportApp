import { PaginatedRoute } from "@/shared/types/route";
import { defineStore } from "pinia";
import { ref, Ref } from "vue";

export const useRouteStore = defineStore('route-store', () => {
    const routeData: Ref<PaginatedRoute | null> = ref(null);
    
    const setRouteData = (data: PaginatedRoute) => {
        routeData.value = data;
    };

    const minusAvailableSeat = (id: number) => {
        if(!routeData.value?.content) return;

        const routeIndex = routeData.value.content.findIndex(route => route.id === id);
        if(routeIndex === -1) return;

        if (routeData.value.content[routeIndex].availableSeats > 0) {
            routeData.value.content[routeIndex].availableSeats -= 1;
        }
    }   

    const getRouteData = (): PaginatedRoute | null => {
        return routeData.value ? routeData.value : null;
    };

    return {
        routeData,
        setRouteData,
        getRouteData,
        minusAvailableSeat
    }
})