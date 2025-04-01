import { defineStore } from "pinia";
import { ref } from "vue";

export const useLoginStore = defineStore('auth', () => {
    const logined = ref(false);
    function auth(){
        logined.value = true
    }
    function logout() {
        logined.value = false;
    }

    return {logined, auth, logout}
    
})