import { defineStore } from "pinia";

export const useModalStore = defineStore('modal-store', () => {
    const isOpen = false;
    return { isOpen }
})