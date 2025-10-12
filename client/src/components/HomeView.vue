<template>
  <div v-if="isAuthFormOpen" class="modal-auth-form">
    <div class="auth-form">
      <SignUpView v-if="currentForm === 'register'" />
      <SignInView v-else-if="currentForm === 'login'" />
    </div>
  </div>

  <!-- header  -->
  <Notifications ref="notifications" />
  <div class="header-container-custom">
    <div class="main-header">
      <div class="navbar-custom-header">
        <div class="navbar-subheader">
          <div style="cursor: pointer;" class="brand" @click="$router.replace('/home')">
            ololotravel
          </div>
          <div class="profile-header-custom">
            <ProfileButtonView />
          </div>
        </div>
      </div>
      <div class="title-subheader-container">
        <div class="title-container">
          <span class="text">
            OloloTravel — с комфортом в любую точку мира.
            <span>🚌</span>
            <span>✈️</span>
            <span>🚂</span>
          </span>
        </div>
        <div class="button-container">
          <button @click="$router.push('/routes')">
            Все маршруты
          </button>
          <button @click="$router.push('/routes/search')">
            Поиск маршрутов
          </button>
        </div>
      </div>
    </div>
    <div 
    class="search-container" 
    :class="{ 'sticky-search': isSticky }" 
    ref="searchContainer">
      <div class="sub-header-container">
        <SearchContainerView />
      </div>
    </div>
  </div>

  <!-- main content -->
  <div class="content">
    <div class="custom-container">
      <route-container />
    </div>
    <div class="footer">
      <div>l1rn</div>
      <div>
        <a href="https://github.com/l1rn" target="_blank"><img :src="github" alt="Логотип"></a>
      </div>
      <div>2025</div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Notifications from './UI/NotificationsView.vue';
import github from '@/assets/github-mark.svg';
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
import SearchContainerView from './molecule/SearchContainerView.vue';
import RouteContainer from './UI/routecomponents/RouteContainerView.vue';
import SignInView from './molecule/auth/SignInView.vue';
import SignUpView from './molecule/auth/SignUpView.vue';
import ProfileButtonView from './molecule/ProfileButtonView.vue';
import { useAuthForms } from '@/composable/useAuthForms';

const isSticky = ref<boolean>(false);
const isScrolled = ref<boolean>(false);
const searchContainer = ref<HTMLElement | null>(null);

const { currentForm, isAuthFormOpen } = useAuthForms();

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50;
  if (searchContainer.value) {
    const header = document.querySelector('.main-header');
    if (header) {
      const headerRect = header.getBoundingClientRect();
      isSticky.value = headerRect.bottom <= 0;
    }
  }
};

const notifications = ref(null);

onMounted(async () => {
  window.addEventListener('scroll', handleScroll);
  nextTick(() => {
    handleScroll();
  })
});

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll);
});

</script>
<style scoped lang="sass">
@import '@/assets/styles/home.sass'
</style>