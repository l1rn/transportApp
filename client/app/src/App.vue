<template>
  <Routes />
  <AppUsersSignUp />
  <AppUsersSignIn />
  <New />
  <GetBooking />
</template>

<script setup>
import Routes from './components/Routes.vue';
import AppUsersSignUp from './components/Signup.vue';
import AppUsersSignIn from './components/Signin.vue';
import New from './components/New.vue';
import GetBooking from './components/GetBooking.vue';
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
