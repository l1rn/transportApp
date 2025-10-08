<template>
  <div v-if="isAuthFormOpen" class="modal-auth-form">
    <div class="auth-form">
      <Signup v-if="currentForm === 'register'" />
      <Signin v-else-if="currentForm === 'login'" />
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
            <custom-profile @open-auth="showLoginForm = true" @logout="userLogout" />
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
import { useRouter } from 'vue-router';
import SearchContainerView from './UI/routecomponents/SearchContainerView.vue';
import RouteContainer from './UI/routecomponents/RouteContainerView.vue';
import Signup from '@/components/UI/auth/SignupView.vue';
import Signin from "@/components/UI/auth/SigninView.vue";
import CustomProfile from "@/components/UI/auth/ProfileView.vue";
import LogoutService from "@/services/LogoutService";
import { cancelTokenRefresh } from "@/services/api";
import { useLoginStore } from '@/stores/authStore';
import { storeToRefs } from 'pinia';
import { useAuthForms } from '@/composable/useAuthForms';

const router = useRouter();
const showLoginForm = ref(false);

const isSticky = ref<boolean>(false);
const isScrolled = ref<boolean>(false);
const searchContainer = ref<HTMLElement | null>(null);

const { currentForm, isAuthFormOpen } = useAuthForms();

const loginStore = useLoginStore()

const { logined } = storeToRefs(loginStore);

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

const showMessage = (type: string, message: string) => {
  notifications.value.showNotification(type.split(':')[0], message);
};

const userLogout = async () => {
  try {
    const success = await LogoutService.logoutUser();
    if (success) {
      cancelTokenRefresh();
      showMessage('success:logout', '✅ Успешный выход!');
      loginStore.logout();
      console.log(logined.value)
      router.push('/');
    }
  } catch (error: any) {
    const message = error.response?.data?.message || 'Ошибка выхода';
    showMessage('error', `${message}`);
  }
};

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