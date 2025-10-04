import { defineStore } from "pinia";
import { ref } from 'vue';

interface Modals {
    [key: string]: boolean;
}

export const useModalStore = defineStore('modal-store', () => {
    const modals = ref<Modals>({});

    const toggle = (key: string) => {
        modals.value[key] = !modals.value[key];
    }

    const close = (key: string) => {
        modals.value[key] = false;
    }

    const isOpen = (key: string): boolean => {
        return !!modals.value[key];
    }

    return { modals , toggle, close, isOpen }
})