<template>
  <div class="grid-wrapper">
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">ID</div>
        <div class="grid-cell">РОЛЬ</div>
        <div class="grid-cell">ИМЯ ПОЛЬЗОВАТЕЛЯ</div>
        <div class="grid-cell"></div>
      </div>
      <div class="grid-content">
        <template v-for="user in users" :key="user.id">
          <div class="grid-row">
            <div class="grid-cell">{{ user.id }}</div>
            <div class="grid-cell">{{ formatRole(user.role) }}</div>
            <div class="grid-cell">{{ user.username }}</div>
            <div class="grid-cell button-container">
              <button v-if="roleNotAdmin.find((u) => u.id === user.id)?.notAdmin" class="give-permission-button"
                @click="getPermissionAdmin(user.id)">
                Дать права
              </button>
              <button :disabled="user.role === 'ROLE_ADMIN' || deletingId === user.id" class="delete-user-button"
                @click="deleteUser(user.id)">
                <span v-if="deletingId === user.id">Удаление...</span>
                <span v-else>Удалить</span>
              </button>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<script setup>
import { adminService } from "@/shared/services/adminService";
import { onMounted, ref, computed } from "vue";

const users = ref([]);
const loading = ref(false);
const allUsers = async () => {
  try {
    loading.value = true;
    const response = await adminService.getAllUsers();
    users.value = response.data;
    console.log(users.value);
  } catch (error) {
  } finally {
    loading.value = false;
  }
};

const getPermissionAdmin = async (userId) => {
  try {
    await adminService.postSetRoleAdmin(userId);
    const response = await adminService.getAllUsers();
    users.value = response.data;
  } catch (error) {
    console.log(error);
  }
};

const roleNotAdmin = computed(() => {
  return users.value.map((user) => ({
    id: user.id,
    notAdmin: user.role !== "ROLE_ADMIN",
  }));
});

const deletingId = ref(null);
const deletedUser = ref(null);
const deleteUser = async (userId) => {
  let userIndex;
  try {
    deletingId.value = userId;
    userIndex = users.value.findIndex((user) => user.id === userId);
    if (userIndex === -1) {
      throw new Error("Пользователь не найден");
    }
    deletedUser.value = users.value[userIndex];
    users.value.splice(userIndex, 1);

    await adminService.deleteUser(userId);
  } catch (error) {
    if (deletedUser.value && userIndex !== -1) {
      users.value.splice(userIndex, 0, deletedUser.value);
    }
  } finally {
    deletingId.value = null;
  }
};

const formatRole = (role) => {
  switch (role) {
    case "ROLE_USER":
      return "Пользователь";
    case "ROLE_ADMIN":
      return "Админ";
    default:
      return "никто";
  }
};

onMounted(() => {
  allUsers();
});
</script>

<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

$grid-columns: 0.5fr 1fr 2fr 2fr;

.grid-wrapper {}

.grid-table {

  .grid-header,
  .grid-row {
    display: grid;
    grid-template-columns: $grid-columns;
  }

  .grid-header {
    background: #d9d5f8;
    border-radius: 16px 16px 0 0;
    color: #111111;
    font-weight: 600;
  }

  .grid-row {
    background: #ffffff;
    transition: all 0.3s ease;

    &:nth-child(even) {
      background: #f5f6fa;
    }

    &:last-child {
      border-radius: 0 0 16px 16px;
    }

    &:hover {
      background: #ede7f6;
    }
  }

  .grid-cell {
    @include mixins.display-center();
    text-align: center;
    padding: .5rem;
  }

  .button-container {
    display: flex;
    gap: 0.5rem;
    button {
      width: 100%;
      padding: 0.5rem 0;
      font-size: 1.05rem;
      border-radius: 0.5rem;
      font-weight: 600;
      transition: all 0.3s ease;
    }

    .give-permission-button {
      @include mixins.button-clear(#6A9F6F, white);
      

      &:hover {
        background: #5C8F61;
      }
    }

    .delete-user-button {
      @include mixins.button-clear(#C46A6F, white);
      &:hover{
        background: #B05A5F;
      }
      &:disabled{
        @include mixins.button-clear(colors.$medium-grey, white);
        cursor: not-allowed;
      }
    }
  }
}
</style>
