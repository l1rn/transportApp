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
        <h1>
          Авторизация
        </h1>
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
          <label for="button-register">Еще не авторизовались? </label>
          <button @click.stop="switchForms">
            Регистрация
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useModalStore } from "@/shared/stores/useModalStore";
import { UserData } from "@/shared/types/userData";
import { ref } from 'vue';
import { useConditionalClickOutside } from "@/composable/useConditionalClickOutside";
import { useAuthForms } from "@/composable/useAuthForms";
import { authorizationService } from "@/shared/services/authorizationService";
import notification from "@/shared/plugins/notifications";
import { useAuthStore } from "@/shared/stores/useLoginStore";

const modalStore = useModalStore();
const user = ref<UserData>({
  username: '',
  password: ''
});

const authStore = useAuthStore();
const { switchForms } = useAuthForms();

const signIn = async () => {
  if (!user.value.username || !user.value.password){
    notification.error("Заполните все поля!");
    return;
  }
  try {
    const response = await authorizationService.signInUser(user.value);

    user.value.username = '';
    user.value.password = '';
    if(response.status === 200){
      authStore.login();
      modalStore.close('login');
      notification.success('Вход выполнен!');
    }
  }
  catch (error: any) {
    console.log(error.message);
    notification.error('Возможно вы ввели данные неправильно!');
  }
}
const containerRef = ref<HTMLElement | null>(null);
useConditionalClickOutside(
  containerRef, 
  () => modalStore.isOpen('login'), 
  () => modalStore.close('login')
);
</script>

<style scoped lang="scss">
@use '../../../assets/styles/molecule/auth/signForm.sass';
</style>