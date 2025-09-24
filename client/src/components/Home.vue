<template>
  <!-- header  -->
  <Notifications ref="notifications" />
  <div class="header-container-custom">
    <div
      class="main-header"
      :class="{ 'header-scrolled': isScrolled }"
    >
      <div class="navbar-custom-header">
        <div class="navbar-subheader">
          <div
            style="cursor: pointer;"
            class="brand"
            @click="$router.replace('/home')"
          >
            ololotravel
          </div>
          <div class="profile-header-custom">
            <custom-profile 
              @open-auth="showLoginForm = true" 
              @logout="userLogout"
            />
          </div>
        </div>
      </div>
      <div class="header-title-container">
        OloloTravel — с комфортом в любую точку мира.
        <span>🚌</span>
        <span>✈️</span>
        <span>🚂</span>
      </div>
      <div class="sub-header-items">
        <div class="header-item">
          <button @click="$router.push('/routes')">
            Все маршруты
          </button>
        </div>
        <div class="header-item">
          <button @click="$router.push('/routes/search')">
            Поиск маршрутов
          </button>
        </div>
      </div>
    </div>
    <div class="search-container">
      <div
        class="sub-header-container"
        :class="{ 'sub-header-fixed': isScrolled }"
      >
        <smart-input 
          @transport-selected="handleTransportSelect" 
          @search-results="handleResults"
          @search-start="showLoading"
        />
      </div>
    </div>
  </div>

  <!-- auth form -->
  <BModal
    v-model="showLoginForm"
    class="b-modal"
    title="Регистрация профиля"
    size="xl"
    no-footer
    no-stacking
  >
    <div class="status-messages">
      <transition name="slide">
        <div
          v-if="responses.success.register"
          class="success-message"
        >
          Успешная регистрация!
        </div>
      </transition>
      <transition name="slide">
        <div
          v-if="responses.error"
          class="error-message"
        >
          {{ responses.error }}
        </div>
      </transition>
    </div>
    <b-tabs
      class="b-tabs"
      content-class="mt-3"
      fill
    >
      <b-tab
        class="nav-link"
        title="Войти"
      >
        <Signin
          @logined="handleUserLogined"
          @close="showLoginForm = false"
        />
      </b-tab>
      <b-tab
        class="nav-link"
        title="Зарегистрироваться"
      >
        <Signup @registered="handleUserRegistered" />
      </b-tab>
    </b-tabs>
  </BModal>
  <div
    class="content"
    :class="{ 'content-padded': isScrolled }"
  >
    <div class="custom-container">
      <route-container
        :search-results="searchResults"
        @update-seats="handleSeatsUpdate"
        @require-auth="handleAuthRequired"
      />
    </div>
    <div class="footer">
      <div>l1rn</div>
      <div>
        <a
          href="https://github.com/l1rn"
          target="_blank"
        ><img
          :src="github"
          alt="Логотип"
        ></a>
      </div>
      <div>2025</div>
    </div>
  </div>
</template>
<script setup>
import Notifications from './UI/Notifications.vue';
import github from '@/assets/github-mark.svg';
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { BModal, BTab, BTabs } from 'bootstrap-vue-next';
import SmartInput from "@/components/UI/routecomponents/SmartInput.vue";
import RouteContainer from './UI/routecomponents/RouteContainer.vue';
import Signup from '@/components/UI/auth/Signup.vue';
import Signin from "@/components/UI/auth/Signin.vue";
import CustomProfile from "@/components/UI/auth/Profile.vue";
import LogoutService from "@/services/LogoutService";
import { cancelTokenRefresh, scheduleTokenRefresh } from "@/services/api";
import { useLoginStore } from '@/stores/authStore';
import { storeToRefs } from 'pinia';

const router = useRouter();
const showLoginForm = ref(false);
const scrollY = ref(0);
const isScrolled = ref(false);
const handleSeatsUpdate = (routeId) => {
  searchResults.value = searchResults.value.map(route => {
    if (route.id === routeId && route.availableSeats > 0) {
      return {
        ...route,
        availableSeats: route.availableSeats - 1
      };
    }
    return route;
  })
}
const loginStore = useLoginStore()

const { logined } = storeToRefs(loginStore);

const responses = ref({
  success: {
    register: false,
    logout: false,
  },
  error: null,
});

const handleAuthRequired = () => {
  showLoginForm.value = true;
  showMessage('error', 'Для бронирования необходимо авторизоваться');
};

const handleScroll = () => {
  scrollY.value = window.scrollY || document.documentElement.scrollTop;
  isScrolled.value = scrollY.value > 100;
};

const notifications = ref(null);

const showMessage = (type, message) => {
  notifications.value.showNotification(type.split(':')[0], message);
};

const handleUserRegistered = (result) => {
  if (result.success) {
    showMessage('success:register', result.message);
    showLoginForm.value = true;
  } else {
    showMessage('error', `${result.message}`);
  }
};

const handleUserLogined = async (result) => {
  if (result.success) {
    showMessage('success:login', ` ${result.message}`);
    loginStore.auth();
    console.log(logined.value)
    scheduleTokenRefresh();
    showLoginForm.value = false;
  } else {
    showMessage('error', `${result.message}`);
  }
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
  } catch (error) {
    const message = error.response?.data?.message || 'Ошибка выхода';
    showMessage('error', `${message}`);
  }
};

onMounted(async () => {
  window.addEventListener('scroll', handleScroll);
  scheduleTokenRefresh();
});

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll);
});
const searchResults = ref([]);
const handleResults = (results) => {
  searchResults.value = results;
}

const isLoading = ref(false);
const showLoading = (state) => {
  isLoading.value = state;
};

</script>
<script>
export default {
  name: "AppHome"
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/home.sass'
</style>