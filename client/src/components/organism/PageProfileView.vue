<template>
  <div class="profile-page">
    <div class="header-wrapper">
      <div class="header-container">
        <div class="title-container">
          <span>👤 Профиль: {{ username }}</span>
        </div>
        <div class="space"></div>
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
    </div>

    <div class="main-container-profile">
      <template v-if="nav.chooseOrders">
        <BookingsWrapperView />  
      </template>
      <template v-if="nav.chooseSettings">
          <UserSettingsControlView :user-info="userInfo" />
        </template>
      <template v-if="nav.chooseModeration">
        <AdminPanelView />
      </template>
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

watch(userInfo, (newValue: UserInfo | null) => {
  if(newValue?.role === "ROLE_ADMIN"){
    hasRoleAdmin.value = true;
  }
  else{
    hasRoleAdmin.value = false;
  }
})
</script>
<style scoped lang="scss">
@import "../../assets/styles/static/mixin.d.scss";
@import "../../assets/styles/static/color.d.scss";

.profile-page {
  .header-wrapper{
    display: flex;
    justify-content: center;
    width: 100%;
    .header-container {
      width: 80%;
      padding: 1rem 1.5rem;
      margin: 1rem 0;
      border-radius: 16px;
      box-shadow: $input-shadow;
      background: $white;
      .title-container{
        span {
          text-transform: uppercase;
          letter-spacing: 0.05rem;
          font-size: 1.5rem;
        }
      }
      .space {
        width: 100%;
        background: $light-gray;
        height: 2px;
        margin: 2rem 0 0 0;
      }
      .nav-tabs {
        button {
          @include button-clear($main-white);
          font-size: 1.25rem;
          padding: 0.5rem 1rem;
        }
        .active {
          border-bottom: 2px solid $primary-blue;
        }
      }
    } 
  }
}
</style>