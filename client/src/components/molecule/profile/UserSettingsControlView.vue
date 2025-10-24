<template>
  <div class="main-container">
    <div class="change-password-container">
      <ChangePasswordFormView />
    </div>
    <div class="info-container">
      <div>
        <h2 class="subtitle-position1">
          Текущая сессия
        </h2>
      </div>
      <div
        v-for="device in props.userInfo.devices"
        :key="device.id"
      >
        <div
          v-if="device.id === deviceId"
          class="useragent-container"
        >
          {{ device.userAgent }}
        </div>
      </div>
      <div>
        <h2 class="subtitle-position2">
          Сессии
        </h2>
      </div>
      <div 
        v-for="device in props.userInfo.devices"
        :key="device.id" 
        class="useragent-container"
      >
        <div 
          class="device-item"
        >
          {{ device.userAgent }}
        </div>
        <div class="delete-item">
          <button 
            @click="deleteSession(device.id)"
          >
            Удалить
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import ChangePasswordFormView from '@/components/atom/ChangePasswordFormView.vue';
import { authorizationService } from '@/services/authorizationService';
import { userService } from '@/services/userService';
import { UserInfo } from '@/types/userData';
import { ref} from 'vue';

const props = defineProps<{
  userInfo: UserInfo;
}>();

const deviceId = ref();

const deleteSession = async(id: number) => {
  try{
    if(deviceId.value === id) {
      await userService.deleteSession(id);
      await authorizationService.logoutUser();
    }
  }
  catch(e){
    console.error(e)
  }
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/molecule/profile/user-setting.sass'
</style>