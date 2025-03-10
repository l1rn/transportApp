<template>
    <div>
        <table class="table-custom">
            <thead>
                <tr>
                <th>ID</th>
                <th>Права</th>
                <th>Имя пользователя</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                <td data-label="ID">{{ user.id }}</td>
                <td data-label="Права">
                    <span :class="'role-badge--' + user.role">{{ formatRole(user.role) }}</span>
                </td>
                <td data-label="Имя пользователя">{{ user.username }}</td>
                </tr>
            </tbody>
        </table>   
    </div>
</template>
<script setup>
import AdminService from '@/services/AdminService';
import { onMounted, ref } from 'vue';
const users = ref([]); 
const allUsers = async() =>{
    try{
        const response = await AdminService.getAllUsers();
        users.value = response.data;
        console.log(users.value);
    }catch(error){
        console.log(error);
    }
}

const formatRole = (role) =>{
    switch(role){
        case 'ROLE_USER': return 'Пользователь'
        case 'ROLE_ADMIN': return 'Админ'
        default: return 'никто'
    }
}

onMounted(() =>{
    allUsers();
})
</script>
<script>
export default {
    name:"AdminAllUsers"
}
</script>
<style lang="sass">
@import "@/assets/styles/adminObjects/all-users-table"
</style>