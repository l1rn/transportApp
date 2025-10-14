<template>
  <div class="main-container">
    <div class="info-container">
      <form
        class="change-password"
        @submit.prevent="changePassword"
      >
        <label
          class="form-title"
          for=""
        >СМЕНА ПАРОЛЯ</label>
        <label for="old">Старый пароль</label>
        <input 
          id="old"
          v-model="passwordRequest.oldPassword" 
          type="password"
          placeholder="старый пароль"
          required
        >
        <label for="new">Новый пароль</label>
        <input 
          id="new"
          v-model="passwordRequest.newPassword"
          type="password"
          placeholder="новый пароль"
          required
        >
        <label for="passwordConfirm">Подтвердите пароль</label>
        <input 
          id="passwordConfirm"
          v-model="confirmPassword"
          type="password"
          placeholder="подтвердите пароль"
          required
        >
        <div
          v-if="!doPasswordMatch"
          class="password-do-not-match"
        >
          Пароли не совпадают
        </div>
        <button 
          type="submit"
          class="submit-button"
          :disabled="!doPasswordMatch"
          :class="{'disabled': !doPasswordMatch}"
        >
          Подтвердить
        </button>
      </form>
      <div>
        <h2 class="subtitle-position1">
          Текущая сессия
        </h2>
      </div>
      <div
        v-for="device in userData.devices"
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
        v-for="device in userData.devices"
        :key="device" 
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
<script setup>
import { authorizationService } from '@/services/authorizationService';
import UserService from '@/services/userService';
import { ref, computed } from 'vue';

const passwordRequest = ref({
    oldPassword: '',
    newPassword: ''
})

const confirmPassword = ref('')

const doPasswordMatch = computed(() => {
    return passwordRequest.value.newPassword === confirmPassword.value
})

const changePassword = async() => {
    if(!doPasswordMatch.value) return;
    try{
        await UserService.changeUserPassword(passwordRequest.value);
        passwordRequest.value = ''
        confirmPassword.value = ''
    } catch(e){
        console.log(e)
        passwordRequest.value = ''
        confirmPassword.value = ''
    }
}

const deviceId = ref();

const deleteSession = async(id) => {
    try{
        if(deviceId.value === id) {
            await UserService.deleteSession(id);
            await authorizationService.logoutUser();
        }
        else {
            await UserService.deleteSession(id);
        }
    }
    catch(e){
        console.log(e)
    }
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/usersObjects/userSetting.sass'
</style>