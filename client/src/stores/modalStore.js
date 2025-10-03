import { defineStore } from "pinia";
import { ref } from 'vue';

export const useModalStore = defineStore('modal-store', () => {
    const modals = ref({});

    const toggle = (key) => {
        modals.value[key] = !modals.value[key];
    }

    const close = (key) => {
        modals.value[key] = false;
    }

    const isOpen = (key) => {
        return !!modals.value[key];
    }

    return { modals , toggle, close, isOpen }
})