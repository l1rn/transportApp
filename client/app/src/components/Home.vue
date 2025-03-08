<template>

  <!-- header  -->
  <div class="header-container-custom">
    <div class="navbar-custom fixed-top shadow-navbar-custom">
      <BNavbar>
        <BNavbarBrand class="brand" @click="this.$router.replace('/home')">ololotravel</BNavbarBrand>
        <BNavbarNav>
          <custom-profile @open-auth="showLoginForm = true"
                          @logout="userLogout"
                          :is-authenticated="isAuthenticated"
          />
        </BNavbarNav>
      </BNavbar>
      <transition name="slide">
        <div v-if="responses.success.login" class="success-message">
          Успешный вход!
        </div>
      </transition>
      <BNavbar>
        <BNav>
          <BNavbarNav class="ms-auto">
            <BNavItemDropdown
                text="Транспорт"
                toggle-class="transport-btn"
                right
                menu-class="transport-menu">
              <BDropdownItem class="transport-item" @click="selectTransport('Поезд')">
                <span class="emoji">🚂</span> Поезд
              </BDropdownItem>
              <BDropdownItem class="transport-item" @click="selectTransport('Автобус')">
                <span class="emoji">🚌</span> Автобус
              </BDropdownItem>
              <BDropdownItem class="transport-item" @click="selectTransport('Авиа')">
                <span class="emoji">✈️</span> Авиа
              </BDropdownItem>
            </BNavItemDropdown>
          </BNavbarNav>
        </BNav>
        <BInput class="b-form-input" placeholder="Откуда"></BInput>
        <BInput class="b-form-input ms-1" placeholder="Куда"></BInput>
        <Datepicker class="ms-1" v-model="date"
                    placeholder="Когда"
                    :format="'dd-MM-yyyy'"
                    :dark="false"
                    :enable-time-picker="false" />
        <Datepicker class="ms-1" v-model="arrivalDate"
                    placeholder="Обратно"
                    :format="'dd-MM-yyyy'"
                    :enable-time-picker="false" />
        <button class="search-button-custom btn"
        :class="{'opacity-50': loading}"
        :disabled="loading">
        <span v-if="!loading">Поиск</span>
        <span v-else>⌛</span>
        <span class="search-icon">🔍</span>
        </button>
      </BNavbar>
      <BNavbar>
        <BNavbarBrand></BNavbarBrand>
        <BNavbarNav style="font-family: Montserrat; margin-right: 20px;" v-if="itemTransport != ''"
                    v-model="itemTransport">Ваш транспорт: {{ itemTransport }}</BNavbarNav>
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

  <!-- section after header  -->

</template>
<script>

import Datepicker from '@vuepic/vue-datepicker';
import {
  BDropdownItem,
  BInput,
  BModal,
  BNav,
  BNavbar,
  BNavbarBrand,
  BNavbarNav,
  BNavItemDropdown,
  BTab,
  BTabs
} from 'bootstrap-vue-next';
import Signup from '@/components/UI/Signup.vue';
import Signin from "@/components/UI/Signin.vue";
import CustomProfile from "@/components/bookings/Profile.vue";
import LogoutService from "@/services/LogoutService";
import {cancelTokenRefresh, scheduleTokenRefresh} from "@/services/api";
export default {
  name: 'AppRoutes',
  components: {
    CustomProfile,
    BDropdownItem,
    BInput,
    BModal,
    BNav,
    BNavbar,
    BNavbarBrand,
    BNavbarNav,
    BNavItemDropdown,
    Datepicker,
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
          login: false
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
          this.$router.push('/');
        }
      }
      catch(error){
        const message = error.response?.data?.message || 'Ошибка выхода';
        this.showMessage('error', `❌ ${message}`, 4000);
      }
    },
    selectTransport(transport){
      this.itemTransport = transport;
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