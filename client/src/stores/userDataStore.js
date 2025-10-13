import { defineStore } from "pinia";
import { ref } from "vue";
import userService from "@/services/userService";

export const useDataSource = defineStore('userData', () => {
    const userData = ref([])

    async function getUserData() {
        try{
            
            userData.value = response.data;
            console.log(userData.value)
        }
        catch(err){
            console.log(err)
        }
    }

    return { userData, getUserData}
})