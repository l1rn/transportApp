import { defineStore } from "pinia";
import { ref } from "vue";
import UserService from "@/services/UserService";

export const useDataSource = defineStore('userData', () => {
    const userData = ref([])

    async function getUserData() {
        try{
            const response = await UserService.getUserAgent();
            userData.value = response.data;
            console.log(userData.value)
        }
        catch(err){
            console.log(err)
        }
    }

    return { userData, getUserData}
})