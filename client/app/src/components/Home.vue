<template>
    <h1>Home page</h1>
    <div class="d-flex flex-nowrap justify-content-center align-items-start ">
        <button class="p-2" @click="$router.push('/routes')">go to routes</button>
        <button class="p-2" @click="$router.push('/auth/sign-up')">register</button>
        <button class="p-2" @click="$router.push('/auth/sign-in')">login</button>
    </div>
    <router-link class="btn btn-primary position-fixed top-0 end-0 m-3" to="/profile">профиль</router-link>
</template>
<script>
export default {
    name: 'AppHome'
}
</script>

<style>
#app {
  font-family: Montserrat;
  color:black;
  text-decoration: none;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

</style>