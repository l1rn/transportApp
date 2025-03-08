<template>

  <!-- header  -->
  <div class="header-container-custom">
    <div class="main-header" :class="{ 'header-scrolled': isScrolled }">
      <div class="navbar-custom-header">
        <div class="brand-header-custom">
          <BNavbar>
            <BNavbarBrand class="brand" @click="this.$router.replace('/home')">ololotravel</BNavbarBrand>
            <BNavbarNav>
              <div v-if="responses.success.login" class="success-message">
                Успешый вход!
              </div>

            </BNavbarNav>
            <div class="profile-header-custom">
              <custom-profile @open-auth="showLoginForm = true"
                              @logout="userLogout"
                              :is-authenticated="isAuthenticated"/>
            </div>
          </BNavbar>
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
      <smart-input></smart-input>
      <BNavbar>
        <BNavbarBrand></BNavbarBrand>
        <BNavbarNav style="font-family: Montserrat; margin-right: 20px;"
                    >Ваш транспорт: {{ itemTransport }}</BNavbarNav>
      </BNavbar>
    </div>

  </div>

  <div class="content" :class="{ 'content-padded': isScrolled }">
    Тестовый блок для проверки скроллбара
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
        <sign-in
            @logined="handleUserLogined"
            @close="showLoginForm = false"
        />

      </b-tab>
      <b-tab class="nav-link" title="Зарегистрироваться">
        <sign-up
            @registered="handleUserRegistered"
        />
      </b-tab>
    </b-tabs>

  </BModal>

</template>
<script>

import {
  BModal,
  BNavbar,
  BNavbarBrand,
  BNavbarNav,
  BTab,
  BTabs
} from 'bootstrap-vue-next';
import smartInput from "@/components/UI/SmartInput.vue";
import Signup from '@/components/UI/Signup.vue';
import Signin from "@/components/UI/Signin.vue";
import CustomProfile from "@/components/bookings/Profile.vue";
import LogoutService from "@/services/LogoutService";
import {onBeforeUnmount, onMounted, ref} from "vue";
import {cancelTokenRefresh, scheduleTokenRefresh} from "@/services/api";
export default {
  name: 'AppRoutes',
  components: {
    smartInput,
    CustomProfile,
    BModal,
    BNavbar,
    BNavbarBrand,
    BNavbarNav,
    'sign-up': Signup,
    'sign-in': Signin,
    'b-tabs': BTabs,
    'b-tab': BTab,
  },
  data(){
    return{
      showLoginForm: false,
      registeredUser: null,
      userLogined: null,
      itemTransport: null,
      responses:{
        success: {
          register: false,
          login: false,
          logout: false,
        },
        error:null,
        haveToken: !!localStorage.getItem("refreshToken"),
        token: localStorage.getItem("refreshToken"),
      }
    }
  },
  computed:{
    isAuthenticated(){
      return !!this.responses.token;
    }
  },
  setup() {
    const selectedTransport = ref(null);

    const handleTransportSelect = (transport) => {
      selectedTransport.value = transport;
    }
    const scrollY = ref(0)
    const isScrolled = ref(false)
    const handleScroll = () => {
      scrollY.value = window.scrollY || document.documentElement.scrollTop
      isScrolled.value = scrollY.value > 100
    }

    onMounted(() => {
      window.addEventListener('scroll', handleScroll)
    })

    onBeforeUnmount(() => {
      window.removeEventListener('scroll', handleScroll)
    })

    return { isScrolled }
  },
  methods:{
    showMessage(type, message, timeout = 3000) {
      if(type.includes('success')) {
        this.responses.error = null;
      } else {
        this.responses.success.register = false;
        this.responses.success.login = false;
      }

      const [category, subType] = type.split(':');
      if(category === 'success') {
        this.responses.success[subType] = message;
      } else {
        this.responses.error = message;
      }

      if(timeout > 0) {
        setTimeout(() => {
          if(category === 'success') {
            this.responses.success[subType] = false;
          } else {
            this.responses.error = null;
          }
        }, timeout);
      }
    },
    async handleUserRegistered(result){
      if(result.success){
        this.showMessage('success:register', result.message, 3000);
        this.showLoginForm = true;
      }else{
        this.showMessage('error', `❌ ${result.message}`, 3000);
      }
    },

    async handleUserLogined(result){
      if(result.success){
        this.showMessage('success:login', ` ${result.message}`, 3000);
        localStorage.setItem("refreshToken", result.refreshToken);
        localStorage.setItem("accessToken", result.accessToken);
        scheduleTokenRefresh();
        this.responses.token = result.refreshToken;
        this.responses.haveToken = !!this.responses.token;
        this.showLoginForm = false;
      }
      else {
        this.showMessage('error', `❌ ${result.message}`, 4000);
      }
    },

    async userLogout(){
      try {
        const success = await LogoutService.logoutUser(
            localStorage.getItem('accessToken'),
            localStorage.getItem('refreshToken')
        );
        if(success){
          cancelTokenRefresh();
          this.showMessage('success:login', '✅ Успешный выход!', 3000);
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          this.responses.token = null ;
          this.responses.haveToken = false;
          this.success.logout = true;
          this.$router.push('/');
        }
      }
      catch(error){
        const message = error.response?.data?.message || 'Ошибка выхода';
        this.showMessage('error', `❌ ${message}`, 4000);
      }
    },

  },
  mounted() {
    if (localStorage.getItem("refreshToken")) {
      scheduleTokenRefresh();
    }
  }
}
</script>

<style scoped lang="sass">
@import '@/assets/styles/home.sass'
</style>