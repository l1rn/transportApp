import { defineStore } from "pinia";
import { ref } from "vue";
import { api } from "@/services/api";

export const useRoleStore = defineStore('role', () => {
    const roleEnum = ref({
        NONE_ROLE: "NONE",
        USER_ROLE: "USER",
        ADMIN_ROLE: "ADMIN"
    });

    const currentRole = ref(""); 

    async function getRole() {
        const response = await api.get(`/users/me/role`)
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