<template>

  <!-- header  -->
  <div class="header-container-custom">
    <div class="main-header" :class="{ 'header-scrolled': isScrolled }">
      <div class="navbar-custom-header">
              <div style="cursor: pointer;" class="brand" @click="this.$router.replace('/home')">
                ololotravel
              </div>
                <div v-if="responses.success.login" class="success-message">
                    Успешый вход!
                </div>
                <div class="header-item">
                  <button @click="this.$router.push('/routes')">
                    Все маршруты с фильтром
                  </button>
                </div>
                <!-- <div class="header-item">
                  <button>
                    123
                  </button>
                </div> -->
              <div class="profile-header-custom">
                <custom-profile @open-auth="showLoginForm = true"
                                @logout="userLogout"
                                :is-authenticated="isAuthenticated"/>
              </div>
      </div>
      <div class="header-title-container">
        OloloTravel — с комфортом в любую точку мира.
        <span>🚌</span>
        <span>✈️</span>
        <span>🚂</span>
      </div>
    </div>
    <div class="sub-header-container"
         :class="{ 'sub-header-fixed': isScrolled }">
      <smart-input @transport-selected="handleTransportSelect"></smart-input>
      <BNavbar>
        <label style="font-family: Montserrat; margin-left: 25rem"
                    >Выбран транспорт: {{ itemTransport }}{{emojiTransport}}</label>
      </BNavbar>
    </div>

  </div>

  <!-- auth form -->
  <BModal class="b-modal" v-model="showLoginForm" title="Регистрация профиля" size="xl" no-footer no-stacking>
    <div class="status-messages">
      <transition name="slide">
        <div v-if="responses.success.register" class="success-message">
          Успешная регистрация!
        </div>
      </transition>
      <transition name="slide">
        <div v-if="responses.error" class="error-message">
          {{ responses.error }}
        </div>
      </transition>
    </div>
    <b-tabs class="b-tabs" content-class="mt-3" fill>
      <b-tab class="nav-link" title="Войти">
        <Signin
            @logined="handleUserLogined"
            @close="showLoginForm = false"
        />

      </b-tab>
      <b-tab class="nav-link" title="Зарегистрироваться">
        <Signup
            @registered="handleUserRegistered"
        />
      </b-tab>
    </b-tabs>
  </BModal>
  <div class="content" :class="{ 'content-padded': isScrolled }">
    <route-container></route-container>
  </div>
</template>
<script setup>
import RouteContainer from './bookings/RouteContainer.vue';
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { 
  BModal,
  BNavbar,
  BTab,
  BTabs
} from 'bootstrap-vue-next';
import smartInput from "@/components/UI/SmartInput.vue";
import Signup from '@/components/UI/Signup.vue';
import Signin from "@/components/UI/Signin.vue";
import CustomProfile from "@/components/bookings/Profile.vue";
import LogoutService from "@/services/LogoutService";
import { cancelTokenRefresh, scheduleTokenRefresh } from "@/services/api";

const router = useRouter();

const showLoginForm = ref(false);
const itemTransport = ref(null);
const emojiTransport = ref(null);
const scrollY = ref(0);
const isScrolled = ref(false);
const responses = ref({
  success: {
    register: false,
    login: false,
    logout: false,
  },
  error: null,
  haveToken: !!localStorage.getItem("refreshToken"),
  token: localStorage.getItem("refreshToken"),
});

const isAuthenticated = computed(() => !!responses.value.token);

const handleScroll = () => {
  scrollY.value = window.scrollY || document.documentElement.scrollTop;
  isScrolled.value = scrollY.value > 100;
};

// Методы
const showMessage = (type, message, timeout = 3000) => {
  const [category, subType] = type.split(':');
  if (category === 'success') {
    responses.value.success[subType] = message;
    responses.value.error = null;
  } else {
    responses.value.error = message;
    Object.keys(responses.value.success).forEach(k => {
      responses.value.success[k] = false;
    });
  }

  if (timeout > 0) {
    setTimeout(() => {
      if (category === 'success') {
        responses.value.success[subType] = false;
      } else {
        responses.value.error = null;
      }
    }, timeout);
  }
};

const handleUserRegistered = (result) => {
  if (result.success) {
    showMessage('success:register', result.message);
    showLoginForm.value = true;
  } else {
    showMessage('error', `❌ ${result.message}`);
  }
};

const handleUserLogined = async (result) => {
  if (result.success) {
    showMessage('success:login', ` ${result.message}`);
    localStorage.setItem("refreshToken", result.refreshToken);
    localStorage.setItem("accessToken", result.accessToken);
    scheduleTokenRefresh();
    responses.value.token = result.refreshToken;
    responses.value.haveToken = true;
    showLoginForm.value = false;
  } else {
    showMessage('error', `❌ ${result.message}`);
  }
};

const userLogout = async () => {
  try {
    const success = await LogoutService.logoutUser(
      localStorage.getItem('accessToken'),
      localStorage.getItem('refreshToken')
    );
    
    if (success) {
      cancelTokenRefresh();
      showMessage('success:logout', '✅ Успешный выход!');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      responses.value.token = null;
      responses.value.haveToken = false;
      router.push('/');
    }
  } catch (error) {
    const message = error.response?.data?.message || 'Ошибка выхода';
    showMessage('error', `❌ ${message}`);
  }
};

const handleTransportSelect = (transport) => {
  itemTransport.value = transport.label;
  emojiTransport.value = transport.emoji;
};

// Хуки жизненного цикла
onMounted(() => {
  window.addEventListener('scroll', handleScroll);
  if (localStorage.getItem("refreshToken")) {
    scheduleTokenRefresh();
  }
});

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>
<script>
  export default{
    name:"AppHome"
  }
</script>
<style scoped lang="sass">
@import '@/assets/styles/home.sass'
</style>