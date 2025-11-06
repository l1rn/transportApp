<template>
  <div id="app">
    <template v-if="showHeader" class="header">
      <AsyncHeaderHomeView />
    </template>
    <router-view />
  </div>
</template>
<script setup>
import { computed, defineAsyncComponent } from 'vue';
import { useRoute } from 'vue-router';
const AsyncHeaderHomeView = defineAsyncComponent(() => 
  import('./components/molecule/headers/HeaderHomeView.vue')
)
const route = useRoute();

const showHeader = computed(() => {
  return route.meta.showHeaders !== false;
});

</script>

<style lang="scss">
</style>