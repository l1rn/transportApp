<<<<<<< HEAD:client/src/components/UI/usercomponents/UserSettingsView.vue
<template>
    <Notifications ref="notifications" />
    <div class="main-container">
        <div class="info-container">
            <form class="change-password" @submit.prevent="changePassword">
                <label class="form-title" for="">СМЕНА ПАРОЛЯ</label>
                <label for="old">Старый пароль</label>
                <input 
                id="old"
                type="password" 
                placeholder="старый пароль"
                v-model="passwordRequest.oldPassword"
                required
                >
                <label for="new">Новый пароль</label>
                <input 
                id="new"
                type="password"
                placeholder="новый пароль"
                v-model="passwordRequest.newPassword"
                required>
                <label for="passwordConfirm">Подтвердите пароль</label>
                <input 
                id="passwordConfirm"
                type="password"
                v-model="confirmPassword"
                placeholder="подтвердите пароль"
                required>
                <div
                class="password-do-not-match"
                v-if="!doPasswordMatch">
                    Пароли не совпадают
                </div>
                <button 
                type="submit"
                class="submit-button"
                :disabled="!doPasswordMatch"
                :class="{'disabled': !doPasswordMatch}"
                >Подтвердить</button>
            </form>
            <div>
                <h2 class="subtitle-position1">Текущая сессия</h2>
            </div>
            <div v-for="device in userData.devices"
            :key="device.id"
            >
                <div
                class="useragent-container"
                v-if="device.id === deviceId">
                    {{ device.userAgent }}
                </div>
            </div>
            <div>
                <h2 class="subtitle-position2">Сессии</h2>
            </div>
            <div 
            class="useragent-container"
            v-for="device in userData.devices" 
            :key="device">
                <div 
                class="device-item"
                >
                    {{ device.userAgent }}
                </div>
                <div class="delete-item">
                    <button 
                    @click="deleteSession(device.id)"
                    >Удалить</button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import Notifications from '@/components/UI/NotificationsView.vue'
import LogoutService from '@/services/LogoutService';
import UserService from '@/services/UserService';
import { useDataSource } from '@/stores/userDataStore';
import { storeToRefs } from 'pinia';
import { ref, onMounted, computed } from 'vue';

const userStore = useDataSource();
const { userData } = storeToRefs(userStore)

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
        showMessage("success", "Пароль успешно изменен")
    } catch(e){
        console.log(e)
        passwordRequest.value = ''
        confirmPassword.value = ''
        showMessage("error", "Ошибка! Вы не смогли сменить пароль")
    }
}

const deviceId = ref();

const checkSession = async() => {
    try{
        const response = await UserService.checkSession();  
        deviceId.value = response.data.deviceId
        console.log(deviceId.value);
    }catch(e){
        console.log(e)
        showMessage("error", "Не получилось получить сессию")
        try{
            await UserService.checkAuth();
        }
        catch{
            try{
                await UserService.refreshIfCheckAuth()
            }
            catch{
                await LogoutService.logoutUser();
            }
        }
    }
}
const deleteSession = async(id) => {
    checkSession();
    try{
        if(deviceId.value === id) {
            await UserService.deleteSession(id);
            await LogoutService.logoutUser();
            showMessage("success", "Сессия успешна удалена!")
        }
        else {
            await UserService.deleteSession(id);
            showMessage("success", "Сессия успешна удалена!")
        }
    }
    catch(e){
        console.log(e)
        showMessage("error", "Ошибка! Не удалось завершить сессию")
    }
}

onMounted(() => {
    checkSession();
})
const notifications = ref(null);

const showMessage = (type, message) => {
  notifications.value.showNotification(type.split(':')[0], message);
};
</script>
<style scoped lang="sass">
@import '@/assets/styles/usersObjects/userSetting.sass'
=======
<template>
  <Notifications ref="notifications" />
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
import Notifications from '@/components/UI/Notifications.vue'
import LogoutService from '@/services/LogoutService';
import UserService from '@/services/UserService';
import { useDataSource } from '@/stores/userDataStore';
import { storeToRefs } from 'pinia';
import { ref, onMounted, computed } from 'vue';

const userStore = useDataSource();
const { userData } = storeToRefs(userStore)

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
        showMessage("success", "Пароль успешно изменен")
    } catch(e){
        console.log(e)
        passwordRequest.value = ''
        confirmPassword.value = ''
        showMessage("error", "Ошибка! Вы не смогли сменить пароль")
    }
}

const deviceId = ref();

const checkSession = async() => {
    try{
        const response = await UserService.checkSession();  
        deviceId.value = response.data.deviceId
        console.log(deviceId.value);
    }catch(e){
        console.log(e)
        showMessage("error", "Не получилось получить сессию")
        try{
            await UserService.checkAuth();
        }
        catch{
            try{
                await UserService.refreshIfCheckAuth()
            }
            catch{
                await LogoutService.logoutUser();
            }
        }
    }
}
const deleteSession = async(id) => {
    checkSession();
    try{
        if(deviceId.value === id) {
            await UserService.deleteSession(id);
            await LogoutService.logoutUser();
            showMessage("success", "Сессия успешна удалена!")
        }
        else {
            await UserService.deleteSession(id);
            showMessage("success", "Сессия успешна удалена!")
        }
    }
    catch(e){
        console.log(e)
        showMessage("error", "Ошибка! Не удалось завершить сессию")
    }
}

onMounted(() => {
    checkSession();
})
const notifications = ref(null);

const showMessage = (type, message) => {
  notifications.value.showNotification(type.split(':')[0], message);
};
</script>
<style scoped lang="sass">
@import '@/assets/styles/usersObjects/userSetting.sass'
>>>>>>> 8a6cce314fb11973cf56c7551e6cfb08585b32bb:client/src/components/UI/usercomponents/UserSettings.vue
</style>