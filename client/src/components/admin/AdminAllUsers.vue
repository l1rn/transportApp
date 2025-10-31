<template>
  <div>
    <div
      v-if="loading"
      class="loading-indicator"
    >
      Загрузка данных...
    </div>
    <table class="table-custom">
      <thead>
        <tr>
          <th>ID</th>
          <th>Права</th>
          <th>Имя пользователя</th>
          <th />
          <th />
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
            <button
              v-if="roleNotAdmin.find(u => u.id === user.id)?.notAdmin"
              class="give-permission-button" 
              @click="getPermissionAdmin(user.id)"
            >
              Дать права
            </button>
            <button 
              :disabled="user.role === 'ROLE_ADMIN' || deletingId === user.id"
              class="delete-user-button"
              @click="deleteUser(user.id)"
            >
              <span v-if="deletingId === user.id">Удаление...</span>
              <span v-else>Удалить</span>
            </button>
          </td>
        </tr>
      </tbody>
    </table>   
  </div>
</template>
<script setup>
import { adminService } from '@/shared/services/adminService';
import { onMounted, ref,computed } from 'vue';

const users = ref([]); 
const loading = ref(false);
const allUsers = async() =>{
    try{
        loading.value = true;
        const response = await adminService.getAllUsers();
        users.value = response.data;
        console.log(users.value);
    }catch(error){
    }
    finally{
      loading.value = false
    }
}

const getPermissionAdmin = async(userId) => {
  try{
    await adminService.postSetRoleAdmin(userId);
    const response = await adminService.getAllUsers();
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

const deletingId = ref(null);
const deletedUser = ref(null);
const deleteUser = async(userId) => {
  let userIndex;
  try{
    deletingId.value = userId;
    userIndex = users.value.findIndex(user => user.id === userId);
    if (userIndex === -1) {
      throw new Error('Пользователь не найден');
    }
    deletedUser.value = users.value[userIndex];
    users.value.splice(userIndex, 1);

    await adminService.deleteUser(userId);
  }catch(error){
    if (deletedUser.value && userIndex !== -1) {
      users.value.splice(userIndex, 0, deletedUser.value);
    }
  }
  finally {
        deletingId.value = null;
    }
}


const notifications = ref(null);


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

<style scoped lang="sass">
@use "../../assets/styles/adminObjects/all-users-table"
</style>