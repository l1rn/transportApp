<template>
  <div class="">
    <div class="">
      <h2 class="">
        Регистрация
      </h2>
      <form @submit.prevent="signUp">
        <div class="first">
          <label for="user">Имя пользователя</label>
          <input 
            id="user" 
            v-model="user.username" 
            type="text"
            placeholder="имя пользователя"
          >
        </div>
        <div class="second">
          <label for="pwd">Пароль</label>
          <input 
            id="pwd" 
            v-model="user.password" 
            type="password"
            placeholder="пароль"
          >
        </div>
        <div class="second">
          <label for="pwd">Пароль</label>
          <input 
            id="pwd" 
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
    </div>
  </div>
</template>
<script setup>
import SignupUsersService from "@/services/SignupUsersService";
import { ref, watch } from 'vue';
const user = ref({
  username: '',
  password: '',
  confirmPassword: '',
})

const isLoading = ref(false);
const passwordError = ref(null);

watch(() => {
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
    const response = await SignupUsersService.signupUser(userData);
    console.log(response);

    user.value.username = '';
    user.value.password = '';
    user.value.confirmPassword = '';
  }
  catch (error) {
    console.log(error.message);
  }
  finally {
    isLoading.value = false;
  }
}
</script>

<style scoped lang="sass">
@import '@/assets/styles/authorizationObjects/sign-up-form.sass'
</style>
