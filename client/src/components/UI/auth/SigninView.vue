<template>
  <div
    ref="containerRef"
    class="sign-in-container"
  >
    <div class="main-container">
      <div class="main-wrapper">
        <div class="close">
          <img
            src="../../../assets/icons/close.png"
            alt="close"
            @click="modalStore.close('login')"
          >
        </div>
        <h2>
          Авторизация
        </h2>
        <form @submit.prevent="signIn">
          <div class="text-area">
            <label for="user">Имя пользователя</label>
            <input
              id="user"
              v-model="user.username"
              type="text"
              placeholder="имя пользователя"
            >
          </div>
          <div class="text-area">
            <label for="pwd">Пароль</label>
            <input
              id="pwd"
              v-model="user.password"
              type="password"
              placeholder="пароль"
            >
          </div>
          <button type="submit">
            Авторизоваться
          </button>
        </form>
        <div class="unauthorized-user-container">
          <label for="button-registe">Еще не авторизовались? </label>
          <button @click="modalStore.toggle('register')">
            Регистрация
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import SigninUsersService from "@/services/SigninUsersService";
import { ref } from 'vue';
import { useLoginStore } from '@/stores/authStore';
import { scheduleTokenRefresh } from '@/services/api';
import { useModalStore } from "@/stores/modalStore";
import { UserData } from "@/types/userdata";
import { useConditionalClickOutside } from "@/composable/useConditionalClickOutside";

const loginStore = useLoginStore();

const modalStore = useModalStore();

const user = ref<UserData>({
  username: '',
  password: ''
});

const signIn = async () => {
  console.log(user.value);
  try {
    await SigninUsersService.signInUser(user.value);

    scheduleTokenRefresh();
    loginStore.auth();

    user.value.username = '';
    user.value.password = ''
  }
  catch (error: any) {
    console.log(error.message);
  }
}
const containerRef = ref<HTMLElement | null>(null);
useConditionalClickOutside(
  containerRef, 
  () => modalStore.isOpen('login'), 
  () => modalStore.close('login')
);
</script>

<style scoped lang="sass">
@import '@/assets/styles/authorizationObjects/sign-in-form.sass'
</style>