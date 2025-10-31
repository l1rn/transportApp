import { PaginatedRoute } from "@/shared/types/route";
import { defineStore } from "pinia";
import { ref, Ref } from "vue";

export const useRouteStore = defineStore('route-store', () => {
    const routeData: Ref<PaginatedRoute | null> = ref(null);
    
    const setRouteData = (data: PaginatedRoute) => {
        routeData.value = data;
    };

    const getRouteData = (): PaginatedRoute | null => {
        return routeData.value ? routeData.value : null;
    };

    return {
        routeData,
        setRouteData,
        getRouteData
    }
})