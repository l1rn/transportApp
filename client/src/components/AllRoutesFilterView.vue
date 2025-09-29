<template>
  <div>
    <div>
      <BackbuttonToHome />
    </div>
    <div>
      <AllRoutesContainer />
    </div>
  </div>
</template>
<script setup>
import AllRoutesContainer from './UI/routecomponents/AllRoutesView.vue';
import BackbuttonToHome from './UI/BackButtonView.vue';
</script>
<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-filter-page"

</style>