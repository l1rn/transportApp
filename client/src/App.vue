<template>
  <div id="app">
    <template v-if="showHeader" class="header">
      <HeaderHomeView />
    </template>
    <router-view />
  </div>
</template>
<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import HeaderHomeView from './components/molecule/headers/HeaderHomeView.vue';

const route = useRoute();

const showHeader = computed(() => {
  return route.meta.showHeaders !== false;
});

</script>

<style lang="scss">
</style>