<template>
  <div>
    <table class="table-custom">
      <thead>
        <tr>
          <th>ID</th>
          <th>Права</th>
          <th>Имя пользователя</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="user in users"
          :key="user.id"
        >
          <td data-label="ID">
            {{ user.id }}
          </td>
          <td data-label="Права">
            <span :class="'role-badge--' + user.role">{{ formatRole(user.role) }}</span>
          </td>
          <td data-label="Имя пользователя">
            {{ user.username }}
          </td>
          <td class="actions">
            <button class="give-permission-button"
            v-if="roleNotAdmin.find(u => u.id === user.id)?.notAdmin" 
            @click="getPermissionAdmin(user.id)">Дать права</button>
            <button 
            :disabled="isRoleAdmin(user.role)"
            class="delete-user-button"
            @click="deleteUser(user.id)">Удалить пользователя</button>
          </td>
        </tr>
      </tbody>
    </table>   
  </div>
</template>
<script setup>
import AdminService from '@/services/AdminService';
import { onMounted, ref,computed } from 'vue';
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

const getPermissionAdmin = async(userId) => {
  try{
    await AdminService.postSetRoleAdmin(userId);
    const response = await AdminService.getAllUsers();
    users.value = response.data;
  }catch(error){
    console.log(error);
  }
}

const roleNotAdmin = computed(() => {
  return users.value.map(user => ({
    id: user.id,
    notAdmin: user.role !== 'ROLE_ADMIN'
  }));
});

const isRoleAdmin = (role) => {
  if(role === 'ROLE_ADMIN') return true
  return false
}

const deleteUser = async(userId) => {
  try{
    await AdminService.deleteUser(userId);
    users.value = users.value.filter(user => user.id !== userId);
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
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/all-users-table"
</style>