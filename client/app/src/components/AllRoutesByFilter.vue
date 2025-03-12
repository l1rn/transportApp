<template>
  <div>
    <div>
      <BackbuttonToHome></BackbuttonToHome>
    </div>
    <div>
      <AllRoutesContainer></AllRoutesContainer>
    </div>
  </div>
</template>
<script setup>
import AllRoutesContainer from './UI/AllRoutesContainer.vue';
import BackbuttonToHome from './UI/BackbuttonToHome.vue';
</script>
<style lang="sass">
@import "@/assets/styles/routeObjects/route-filter-page"

</style>