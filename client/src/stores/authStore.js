import { defineStore } from "pinia";
import { ref } from "vue";

export const useLoginStore = defineStore('auth', () => {
    const logined = ref(false);
    const initLoginState = () => {
        logined.value = localStorage.getItem("logined") === 'true'
    }
    function auth() {
        logined.value = true
        localStorage.setItem('logined', 'true');
    }
    function logout() {
        logined.value = false;
        localStorage.removeItem('logined');
    }
    return {logined, auth, logout, initLoginState}
})