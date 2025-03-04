<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <!-- header  -->
  <div class="header-container-custom">
      <div class="navbar-custom fixed-top">
          <BNavbar>
              <BNavbarBrand class="brand" href="/#">ololotravel</BNavbarBrand>
              <div class="custom-popup" v-if="showPopup" :class="{ fadeOut: isFadingOut }">
                  <div class="popup-content">
                      <p>{{ popupMessage }}</p>
                      <button @click="showPopup = false">OK</button>
                  </div>
              </div>
              <BNavbarNav>
                  <BNavItemDropdown v-model="textProfile" text="👤Профиль">
                      <BNav align="center">
                          <BDropdownItem @click="121">Мои заказы</BDropdownItem>
                          <BDropdownItem @click="formCheck.showLoginForm = true">Авторизация</BDropdownItem>
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
      <BModal v-model="showSignupModal" title="Регистрация профиля" class="xl" hide-footer>
        <sign-up
          @userRegistered="handleUseregistered"
          @close="showSignupModal = false"
        />
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
BNavItemDropdown
} from 'bootstrap-vue-next';
import Signup from './components/Signup.vue';

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
  'sign-up': Signup
  },
  data(){
    return{
      showSignupModal: true,
      registeredUser: null
    }
  },
  methods:{
    handleUseregistered(userData){
      this.registeredUser = userData;
      this.showSignupModal = false;
      console.log('Зарегистрирован пользователь:', userData);
    }
  }
}

</script>

<style scoped>

/* overflow-x:hidden - chrome, firefox, IE, edge, safari, opera*/
::v-deep .dropdown-menu {
overflow-y: hidden; 
scrollbar-width: none;
-ms-overflow-style: none; 
}

::v-deep .dropdown-menu::-webkit-scrollbar {
display: none;
}
/*  */
/* header */
/*  */
.navbar-custom{
  z-index: 1030;
  height: 17vh;
  background: white;
  box-shadow: 0 2px 0 rgba(0,0,0, 0.06);
  padding-left: 15%;
  padding-right: 15%;
}
.search-button-custom{
  border: none;
  max-height: 45px;
  min-width: 200px;
}
/* logo */
.brand{
  color: rgb(27, 27, 27);
  font-size: large;
}
.header-container-custom{
  font-family: Montserrat;
  height: 17vh;
}

/*  */
/* main  */
/*  */

.main-container{
  height: 160vh;
  background: linear-gradient(135deg, #ff9a9e, #fad0c4, #fad0c4, #ffdde1);
  padding-top: 5vh;
}

/* container to search - first task */
.container-route-to-search{
  border-radius: 15px;
  height: 40vh;
  background-color: white;
  margin-top: 5vh;
  margin-right: 15%;
  margin-left: 15%;
}

/* see all routes and sort it - second task */
.container-to-see-all-routes{
  border-radius: 15px;
  height: 40vh;
  background-color: white;
  margin-top: 20vh;
  margin-right: 15%;
  margin-left: 15%;
}

/* custom toast  */

.custom-popup {
position: fixed;
top: 10%;
left: 90%;
transform: translate(-50%, -50%);
background: white;
padding: 20px;
border-radius: 10px;
box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
animation: fadeIn 0.5s ease-in-out;
z-index: 1000;
}

.fadeOut {
animation: fadeOut 0.5s ease-in-out forwards;
}

.popup-content {
text-align: center;
}

.popup-content p {
font-size: 16px;
color: #333;
}

.popup-content button {
margin-top: 10px;
padding: 8px 16px;
border: none;
background-color: #00611d;
color: white;
border-radius: 5px;
cursor: pointer;
}

.popup-content button:hover {
background-color: #004d15;
}

@keyframes fadeIn {
from {
  opacity: 0;
  transform: translate(-50%, -60%);
}
to {
  opacity: 1;
  transform: translate(-50%, -50%);
}
}

@keyframes fadeOut {
from {
  opacity: 1;
  transform: translate(-50%, -50%);
}
to {
  opacity: 0;
  transform: translate(-50%, -60%);
}
}

/* footer */
.footer-container{
  height: 20vh;
  background: #333;
}


</style>