import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

export const useRoleStore = defineStore('role', () => {
    const roleEnum = ref({
        NONE_ROLE: "NONE",
        USER_ROLE: "USER",
        ADMIN_ROLE: "ADMIN"
    });

    let currentRole = ref(""); 

    async function getRole() {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
        if(response.data.role === "ROLE_ADMIN") {
            currentRole.value = roleEnum.value.ADMIN_ROLE;
        } else if(response.data.role === "ROLE_USER"){
            currentRole.value = roleEnum.value.USER_ROLE;
        }
        else{
            currentRole.value = roleEnum.value.NONE_ROLE;
        }
    }
    return {currentRole, getRole}
})