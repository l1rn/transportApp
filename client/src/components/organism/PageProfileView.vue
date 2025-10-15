<template>
  <div class="profile-page">
    <BackButtonView />
    <div class="header-profile">
      <div class="header-profile__title">
        <h1>Профиль - {{ username }}</h1>
      </div>
    </div>

    <div class="main-container-profile">
      <div class="nav-tabs">
        <button
          class="nav-link"
          :class="{ active: nav.chooseOrders}"
          @click="chooseNav('orders')"
        >
          Мои заказы
        </button>
        <button
          class="nav-link"
          :class="{ active: nav.chooseSettings}"
          @click="chooseNav('settings')"
        >
          Настройки
        </button>
        <button
          v-if="!!hasRoleAdmin"
          class="nav-link"
          :class="{ active: nav.chooseModeration}"
          @click="chooseNav('moderation')"
        >
          Модерирование
        </button>
      </div>
    </div>

    <div class="tab-content">
      <div v-if="nav.chooseOrders">
        <BookingsWrapperView />
      </div>
      <div v-if="nav.chooseSettings">
        <UserSettingsControlView />
      </div>
      <div v-if="nav.chooseModeration">
        <AdminPanelView />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import UserSettingsControlView from "../molecule/profile/UserSettingsControlView.vue";
import BackButtonView from "@/components/atom/BackButtonView.vue";
import { onMounted, onUnmounted, ref, watch  } from "vue";
import AdminPanelView from "@/components/admin/AdminPanelView.vue";
import BookingsWrapperView from "@/components/molecule/profile/BookingsWrapperView.vue";
import { userService } from "@/services/userService";
import { AxiosResponse } from "axios";
import { UserInfo } from "@/types/userData";

const username = ref<string>("");
const userInfo = ref<UserInfo | null>(null);

const nav = ref({
  chooseOrders: true,
  chooseSettings: false,
  chooseModeration: false,
})

const chooseNav = (type: string) => {
  nav.value.chooseOrders = type === 'orders';
  nav.value.chooseSettings = type === 'settings';
  nav.value.chooseModeration = type === 'moderation';
}

const hasRoleAdmin = ref(false);

onMounted(async () =>{
  const response: AxiosResponse<UserInfo> = await userService.getUserInfo();
  userInfo.value = response.data;
  username.value = userInfo.value.username;
})

onUnmounted(() => {
  hasRoleAdmin.value = false
})

watch(userInfo, (newValue) => {
  if(newValue?.role === "ROLE_ADMIN"){
    hasRoleAdmin.value = true;
  }
  else{
    hasRoleAdmin.value = false;
  }
})
</script>
<style scoped lang="sass">
</style>