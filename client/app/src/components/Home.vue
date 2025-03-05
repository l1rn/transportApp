<!-- eslint-disable vue/multi-word-component-names -->
<template>

  <!-- header  -->
  <div class="header-container-custom">
    <div class="navbar-custom fixed-top shadow-navbar-custom">
      <BNavbar>

        <BNavbarBrand class="brand" href="/#">ololotravel</BNavbarBrand>
        <div class="custom-popup" v-if="showPopup" :class="{ fadeOut: isFadingOut }">
          <div class="popup-content">
            <p>{{ popupMessage }}</p>
            <button @click="showPopup = false">OK</button>
          </div>
        </div>
        <transition name="slide">
          <div v-if="responses.success.login" class="success-message">
            Успешный вход!
          </div>
        </transition>
        <BNavbarNav>
          <BNavItemDropdown v-model="textProfile" text="👤Профиль">
            <BNav align="center">
              <BDropdownItem @click="this.$router.replace('/profile');">Мои заказы</BDropdownItem>
              <BDropdownItem @click="showLoginForm = true">Авторизация</BDropdownItem>
              <BDropdownItem @click="1231">Выйти</BDropdownItem>
            </BNav>
          </BNavItemDropdown>
        </BNavbarNav>
      </BNavbar>

      <BNavbar >
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
        <BInput placeholder="Откуда"></BInput>
        <BInput class="ms-1" placeholder="Куда"></BInput>
        <Datepicker class="ms-1" v-model="date"
                    placeholder="Когда"
                    :format="'dd-MM-yyyy'"
                    :dark="false"
                    :enable-time-picker="false" />
        <Datepicker class="ms-1" v-model="arrivalDate"
                    placeholder="Обратно"
                    :format="'dd-MM-yyyy'"
                    :enable-time-picker="false" />
        <BButton class="search-button-custom ms-1">Подобрать билеты</BButton>
      </BNavbar>
      <BNavbar>
        <BNavbarBrand></BNavbarBrand>
        <BNavbarNav style="font-family: Montserrat; margin-right: 20px;" v-if="itemTransport != ''"
                    v-model="itemTransport">Ваш транспорт: {{ itemTransport }}</BNavbarNav>
      </BNavbar>
    </div>
  </div>

  <!-- auth form -->
  <BModal v-model="showLoginForm" title="Регистрация профиля" size="xl" no-footer no-stacking>
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
    <b-tabs content-class="mt-3" fill>
      <b-tab title="Войти">
        <sign-in
            @userLogined="handleUserLogined"
            @close="showLoginForm = false"
        />

      </b-tab>
      <b-tab title="Зарегистрироваться"><sign-up
          @userRegistered="handleUseregistered"
      /></b-tab>
    </b-tabs>

  </BModal>

  <!-- section after header  -->
  <div class="main-container">
    <div class="container-route-to-search">
      <BImg>12123</BImg>
    </div>
    <div class="container-to-see-all-routes">

    </div>
  </div>
  <div class="footer-container">
  </div>
  <redirect-view></redirect-view>
</template>
<script>

import Datepicker from '@vuepic/vue-datepicker';
import {
  BButton,
  BDropdownItem,
  BInput,
  BModal,
  BNav,
  BNavbar,
  BNavbarBrand,
  BNavbarNav,
  BNavItemDropdown, BTab, BTabs
} from 'bootstrap-vue-next';
import Signup from '@/components/Signup.vue';
import Signin from "@/components/Signin.vue";
import "@/assets/home.css";
import SignupUsersService from "@/services/SignupUsersService";
import SigninUsersService from "@/services/SigninUsersService";
export default {
  name: 'AppRoutes',
  components: {
    BButton,
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
        error:null
      }
    }
  },
  methods:{
    showMessage(type, message, timeout = 3000) {
      // Сброс сообщений
      if(type.includes('success')) {
        this.responses.error = null;
      } else {
        this.responses.success.register = false;
        this.responses.success.login = false;
      }

      // Установка нового сообщения
      const [category, subType] = type.split(':');
      if(category === 'success') {
        this.responses.success[subType] = message;
      } else {
        this.responses.error = message;
      }

      // Автоскрытие
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
    async handleUseregistered(userData){
      try{
        const response = await SignupUsersService.signupUser(userData);
        console.log(response);
        this.showLoginForm = true;
        this.showMessage('success:register', '✅ Успешная регистрация!', 2000);
        this.error = null;
      }
      catch(error){
        this.responses.success.register = false;
        const message = error.response?.data?.message || 'Ошибка регистрации';
        this.showMessage('error', `❌ ${message}`, 4000);
      }
    },
    async handleUserLogined(userDataLogin){
      try{
        const response = await SigninUsersService.signinUser(userDataLogin);
        this.saveTokens(response);
        this.showMessage('success:login', '✅ Успешный вход!', 2000);
        this.responses.success.login = true;
        this.showLoginForm = false;
      }
      catch(error){
        const message = error.response?.data?.message || 'Ошибка входа';
        this.showMessage('error', `❌ ${message}`, 4000);
      }
    },
    selectTransport(transport){
      this.$emit.responses.register = false;
      this.itemTransport = transport;
    },
    saveTokens(tokens){
      localStorage.setItem('accessToken', tokens.accessToken);
      localStorage.setItem('refreshToken', tokens.refreshToken);
    }
  }

}

</script>

<style scoped>

</style>