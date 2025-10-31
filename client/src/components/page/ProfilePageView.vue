<template>
  <div class="profile-page">
    <HeaderProfilePageView 
      :username="username"
      :balance="balance"
      :has-role-admin="hasRoleAdmin"
    />

    <div class="main-container-profile">
      <template v-if="modalStore.isOpen('profile-page-bookings')">
        <BookingsWrapperView
          :has-email="userInfo?.email !== null" 
        />  
      </template>
      <template v-if="modalStore.isOpen('profile-page-settings')">
          <UserSettingsControlView 
          :user-info="userInfo" />
        </template>
      <template v-if="modalStore.isOpen('profile-page-admin-panel')">
        <AdminPanelView />
      </template>
    </div>
  </div>
</template>
<script setup lang="ts">
import UserSettingsControlView from "../molecule/profile/UserSettingsControlView.vue";
import { onMounted, onUnmounted, ref, watch  } from "vue";
import AdminPanelView from "@/components/admin/AdminPanelView.vue";
import BookingsWrapperView from "@/components/molecule/profile/BookingsWrapperView.vue";
import { userService } from "@/shared/services/userService";
import { AxiosResponse } from "axios";
import { UserInfo } from "@/shared/types/userData";
import HeaderProfilePageView from "../molecule/profile/HeaderProfilePageView.vue";
import { useModalStore } from "@/shared/stores/useModalStore";

const username = ref<string>("");
const balance = ref<number | null>(null);
const userInfo = ref<UserInfo | null>(null);

const modalStore = useModalStore();

const hasRoleAdmin = ref(false);

onMounted(async () =>{
  const response: AxiosResponse<UserInfo> = await userService.getUserInfo();
  userInfo.value = response.data;
  username.value = userInfo.value.username;
  balance.value = userInfo.value.balance;
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
@use "../../assets/styles/static/mixin";
@use "../../assets/styles/static/color";

.profile-page {
  
}
</style>