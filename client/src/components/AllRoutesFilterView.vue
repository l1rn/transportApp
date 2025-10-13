<template>
  <div>
    <div>
      <BackButtonView />
    </div>
    <div>
      <AllRoutesContainer />
    </div>
  </div>
</template>
<script setup>
import AllRoutesContainer from './UI/routecomponents/AllRoutesView.vue';
import BackButtonView from './atom/BackButtonView.vue';
</script>
<style scoped lang="sass">
@import "@/assets/styles/routeObjects/route-filter-page"

</style>