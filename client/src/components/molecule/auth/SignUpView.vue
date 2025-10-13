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
            @click="modalStore.close('register')"
          >
        </div>
        <h1>
          Регистрация
        </h1>
        <form @submit.prevent="signUp">
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
          <div class="text-area">
            <label for="confirm-pwd">Подтвердите пароль</label>
            <input
              id="confirm-pwd"
              v-model="user.confirmPassword"
              type="password"
              placeholder="пароль"
            >
          </div>
          <div v-if="passwordError">
            {{ passwordError }}
          </div>
          <button type="submit">
            Зарегистрироваться
          </button>
        </form>
        <div class="unauthorized-user-container">
          <label for="button-register">Уже зарегистрировались? </label>
          <button @click.stop="switchForms">
            Авторизация
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useAuthForms } from "@/composable/useAuthForms";
import { useConditionalClickOutside } from "@/composable/useConditionalClickOutside";
import { authorizationService } from "@/services/authorizationService";
import { useModalStore } from "@/stores/useModalStore";
import { ref, watch } from 'vue';
const user = ref({
  username: '',
  password: '',
  confirmPassword: '',
})

const { switchForms } = useAuthForms();

const modalStore = useModalStore();

const isLoading = ref(false);
const passwordError = ref<string | null>(null);

watch(
() => [user.value.password, user.value.confirmPassword],  
() => {
  if(user.value.confirmPassword !== user.value.password || user.value.confirmPassword === ''){
    passwordError.value = "Пароли не совпадают!";
  }
  else{
    passwordError.value = null;
  }
})

const signUp = async () => {
  if (passwordError.value) return;
  try {
    isLoading.value = true;

    const userData = {
      username: user.value.username,
      password: user.value.password
    };
    const response = await authorizationService.signupUser(userData);
    console.log(response);

    user.value.username = '';
    user.value.password = '';
    user.value.confirmPassword = '';
  }
  catch (error: any) {
    console.log(error.message);
  }
  finally {
    isLoading.value = false;
  }
}

const containerRef = ref<HTMLElement | null>(null);
useConditionalClickOutside(
  containerRef, 
  () => modalStore.isOpen('register'), 
  () => modalStore.close('register')
);
</script>

<style scoped lang="sass">
@import '@/assets/styles/molecule/auth/sign-form.sass'
</style>
