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

<style scoped lang="scss">
$table-border-color: #e0e0e0;
$table-header-bg: #f5f5f5;
$table-hover-bg: #f9f9f9;
$primary-color: #2196f3;
$secondary-color: #4caf50;
$accent-color: #ff5722;

* {
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.table-custom {
  width: 100%;
  border-collapse: collapse;
  margin: 1.5rem 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);

  .actions {
    display: flex;
    justify-content: center;
    align-items: center;

    button {
      width: 100%;
      padding: 0.3rem 0;
      border: 0;
      border-radius: 10px;
      margin-left: 4px;
      transition: all 0.4s;
      outline: none;

      &:not(:disabled):hover {
        transform: translateY(-1px);
      }
    }

    .give-permission-button {
      box-shadow: 0 0 10px rgba(31, 255, 1, 0.3);
      background: #069636;
      color: #ffffff;
    }

    .delete-user-button {
      background: #940606;
      color: #ffffff;

      &:disabled {
        background: #808080;
        box-shadow: 0;
      }

      &:not(:disabled):hover {
        box-shadow: 0 0 10px rgba(255, 1, 1, 0.3);
      }
    }
  }

  th, td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid $table-border-color;
  }

  thead {
    background-color: $table-header-bg;

    th {
      font-weight: 600;
      text-transform: uppercase;
      font-size: 0.9em;
      color: darken($table-header-bg, 50%);

      &:first-child {
        border-radius: 4px 0 0 0;
      }

      &:last-child {
        border-radius: 0 4px 0 0;
      }
    }
  }

  tbody {
    tr {
      transition: background-color 0.2s ease;

      &:nth-child(even) {
        background-color: lighten($table-header-bg, 3%);
      }

      &:hover {
        background-color: $table-hover-bg;
      }
    }
  }
}

@mixin role-badge($color) {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  background-color: $color;
  color: white;
  font-size: 0.8em;
  font-weight: 500;
}

.role-badge {
  &--admin {
    @include role-badge($accent-color);
  }

  &--moderator {
    @include role-badge($primary-color);
  }

  &--user {
    @include role-badge($secondary-color);
  }
}
</style>