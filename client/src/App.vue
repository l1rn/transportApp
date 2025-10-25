<template>
  <div>
    <!-- todo -->
    <template v-if="showHeader" class="header">
      <HeaderView />
    </template>
    <router-view />
  </div>
</template>
<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import HeaderView from './components/molecule/HeaderView.vue';

const route = useRoute();

const showHeader = computed(() => {
  return route.meta.showHeaders !== false;
});
</script>

<style lang="scss">
</style>