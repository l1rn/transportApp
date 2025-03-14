<template>
  <Notifications ref="notifications"></Notifications>
  <div>
    <div v-if="loading" class="loading-indicator">
      Загрузка данных...
    </div>
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
            :disabled="deletingId === user.id"
            class="delete-user-button"
            @click="deleteUser(user.id)">
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
import AdminService from '@/services/AdminService';
import { onMounted, ref,computed } from 'vue';
import Notifications from '@/components/UI/Notifications.vue'
const users = ref([]); 
const loading = ref(false);
const allUsers = async() =>{
    try{
        loading.value = true;
        const response = await AdminService.getAllUsers();
        users.value = response.data;
        console.log(users.value);
    }catch(error){
      notifications.value?.showNotification('error', 'Ошибка загрузки пользователей');
    }
    finally{
      loading.value = false
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

const deletingId = ref(null);
const deletedUser = ref(null);

const deleteUser = async(userId) => {
  let fullIndex
  if(!confirm('Вы уверены, что хотите удалить пользователя?')) return;
  try{

    deletingId.value = userId;

    fullIndex = users.value.findIndex(u => u.id === userId)
    if (fullIndex === -1) return;

    const hasBooking = await checkUserForBooking(userId)
    if(hasBooking){
      notifications.value.showNotification('error', 'У пользователя есть бронь!')
      return
    }

    deletedUser.value = users.value[fullIndex]
    users.value.splice(fullIndex, 1);

    await AdminService.deleteUser(userId);
    notifications.value.showNotification('success', "Пользователь удален!")
  }catch(error){
    if (deletedUser.value && fullIndex !== -1) {
            users.value.splice(fullIndex, 0, deletedUser.value);
        }
        notifications.value.showNotification(
            'error', 
            error.response?.data?.message || 'Ошибка при удалении маршрута'
        );
  }
  finally {
        deletingId.value = null;
        deletedUser.value = null;
    }
}

const notifications = ref(null);

const checkUserForBooking = async(userId) => {
  try{
    const response = await AdminService.getAllBookings();
    return response.data.some(booking => booking.user.id === userId)
  }catch(error){
    notifications.value.showNotification('error', 'Ошибка при проверке бронирований')
    return false
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