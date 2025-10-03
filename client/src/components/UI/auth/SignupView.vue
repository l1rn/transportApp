<template>
  <div class="container mt-5">
    <div class="card shadow-lg p-4 custom-form">
      <h2 class="text-center mb-4">
        Регистрация
      </h2>
      <form @submit.prevent="signUp">
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
        <div class="second">
          <label for="pwd">Пароль</label>
          <input 
          id="pwd" 
          type="password" 
          placeholder="пароль"
          v-model="user.confirmPassword">
        </div>
        <div v-if="passwordError">{{ passwordError }}</div>
        <button type="submit">Зарегистрироваться</button>
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

<style scoped>

</style>
