import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { userService } from "../services/userService";

export const useAuthStore = defineStore('auth', () => {
    const isAuthenticated = ref(false);
    const isLoading = ref(false);

    const isAuth = computed(() => isAuthenticated.value);

    const checkAuth = async () => {
        isLoading.value = true;
        try {
            const response = await userService.getMyStatus();
            isAuthenticated.value = response.data;
            return response.data;
        } catch (error) {
            isAuthenticated.value = false;
            console.error(error);
            return false;
        } finally {
        isLoading.value = false;
        }
    }

    const login = () => {
        isAuthenticated.value = true;
    }

    const logout = () => {
        isAuthenticated.value = false;
    }

    return {
        isAuthenticated,
        isAuth,
        isLoading,
        checkAuth,
        login,
        logout
    }
})