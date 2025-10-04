<template>
  <div class="sign-in-container">
    <div class="main-container">
      <div class="main-wrapper">
        <div class="close">
          <img @click="modalStore.close('login')" src="../../../assets/icons/close.png" alt="">
        </div>
        <h2>
          Авторизация
        </h2>
        <form @submit.prevent="signIn">
          <div class="text-area">
            <label for="user">Имя пользователя</label>
            <input id="user" type="text" placeholder="имя пользователя" v-model="user.username" />
          </div>
          <div class="text-area">
            <label for="pwd">Пароль</label>
            <input id="pwd" type="password" placeholder="пароль" v-model="user.password">
          </div>
          <button type="submit">Авторизоваться</button>
        </form>
        <div class="unauthorized-user-container">
          <label for="button-registe">Еще не авторизовались? </label>
          <button v-on:click="modalStore.toggle('register')">Регистрация</button>
        </div>
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

<style scoped lang="sass">
@import '@/assets/styles/authorizationObjects/sign-in-form.sass'
</style>