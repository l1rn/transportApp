<template>
    <div class="main-container">
        <h1>Пользователь - {{ userData.username }} </h1>
        <div class="info-container">
            <form class="change-password" @submit.prevent="changePassword">
                <label class="form-title" for="">СМЕНА ПАРОЛЯ</label>
                <label for="old">Старый пароль</label>
                <input 
                id="old"
                type="password" 
                placeholder="старый пароль"
                v-model="passwordRequest.oldPassword"
                >
                <label for="oldConfirm">Подтвердите пароль</label>
                <input 
                id="oldConfirm"
                type="password"
                placeholder="подтвердите пароль">
                <label for="">Новый пароль</label>
                <input 
                type="password"
                placeholder="новый пароль"
                v-model="passwordRequest.newPassword">
                <button 
                type="submit"
                class="submit-button"
                >Подтвердить</button>
            </form>
            <h2>Сессии</h2>
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
                    <button type="submit">Удалить</button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import UserService from '@/services/UserService';
import { ref, onMounted } from 'vue';

const userData = ref([])
const fetchUserDevices = async () => {
    const response = await UserService.getUserAgent();
    userData.value = response.data;
    console.log(userData.value)
}

const passwordRequest = ref({
    oldPassword: '',
    newPassword: ''
})

const changePassword = async() => {
    try{
        await UserService.changeUserPassword(passwordRequest.value);
    }catch(e){
        console.log(e)
    }
}

onMounted(() => {
    fetchUserDevices();
})
</script>
<style scoped lang="sass">
@import '@/assets/styles/usersObjects/userSetting.sass'
</style>