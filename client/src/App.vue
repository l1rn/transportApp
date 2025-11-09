<template>
  <div id="app">
    <template v-if="showHeader" class="header">
      <AsyncHeaderHomeView />
    </template>
    <div class="auth-form" 
    v-if="isAuthFormOpen">
      <AsyncSignUpView v-if="currentForm === 'register'" />
      <AsyncSignInView v-else-if="currentForm === 'login'" />
    </div>
    <router-view />
  </div>
</template>
<script setup>
import { computed, defineAsyncComponent, watchEffect } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthForms } from './composable/useAuthForms';

const { currentForm, isAuthFormOpen } = useAuthForms();

const AsyncSignUpView = defineAsyncComponent(() => 
  import("@/components/molecule/auth/SignUpView.vue")
)

const AsyncSignInView = defineAsyncComponent(() =>
  import("@/components/molecule/auth/SignInView.vue")
)

const AsyncHeaderHomeView = defineAsyncComponent(() => 
  import('./components/molecule/headers/HeaderHomeView.vue')
)
const route = useRoute();

const showHeader = computed(() => {
  return route.meta.showHeaders !== false;
});

watchEffect(() => {
  if(isAuthFormOpen.value){
    document.body.style.overflowY = 'hidden';
    document.documentElement.style.overflowY = 'hidden';
  } else {
    document.body.style.overflowY = 'auto';
    document.documentElement.style.overflowY = 'auto';
  }
});
</script>

<style lang="scss" scoped>
@use "../src/assets/styles/static/mixin" as mixins;

.auth-form {
  @include mixins.display-modal();
  height: 100%;
  z-index: 10000;
}
</style>