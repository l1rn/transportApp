<template>
  <div class="container mt-5">
    <div class="card shadow-lg p-4 custom-form">
      <h2 class="text-center mb-4">
        Авторизация
      </h2>
      <form @submit.prevent="signIn">
        <div class="first">
          <label for="user">Имя пользователя</label>
          <input 
          id="user" 
          type="text" 
          placeholder="имя пользователя"
          v-model="user.username"
          />
        </div>
        <div class="second">
          <label for="pwd">Пароль</label>
          <input 
          id="pwd" 
          type="password" 
          placeholder="пароль"
          v-model="user.password">
        </div>
        <button type="submit">Авторизоваться</button>
      </form>
      <div class="unauthorized-user-container">
        <label for="button-registe">Еще не авторизовались? </label>
        <button v-on:click="modalStore.toggle('register')">Регистрация</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import SigninUsersService from "@/services/SigninUsersService";
import { ref } from 'vue';
import { useLoginStore } from '@/stores/authStore';
import { scheduleTokenRefresh } from '@/services/api';
import { useModalStore } from "@/stores/modalStore";

const loginStore = useLoginStore();

const modalStore = useModalStore();

const user = ref({
  username: '',
  password: ''
});

const signIn = async () => {
  console.log(user.value);
  try {
    await SigninUsersService.signinUser(user.value.username, user.value.password);

    scheduleTokenRefresh();
    loginStore.auth();

    user.value.username = '';
    user.value.password = ''
  }
  catch (error) {
    console.log(error.message);
  }
}
</script>

<style scoped>
</style>