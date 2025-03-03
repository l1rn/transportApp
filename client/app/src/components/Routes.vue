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
        <BModal v-model="formCheck.showLoginForm" title="Регистрация профиля" class="xl" hide-footer>
        <!-- REGISTER  -->
            <BForm v-if="formCheck.showSubRegisterForm" @submit.prevent="signup">
                <p v-if="formCheck.registerSuccess" style="color:green">Вы зарегистрированы!</p>
                <!-- User -->
                <BFormGroup 
                    id="username"
                    label="Имя пользователя"
                    label-for="input-username"
                    class="mb-3">
                    
                    <BFormInput 
                        id="input-username"
                        v-model="userRegister.username"
                        placeholder="пользователь"
                        required
                    />
                </BFormGroup>
                
                <!-- Password -->
                <BFormGroup 
                    id="password"
                    label="Пароль"
                    label-for="input-password"
                    >
                    
                    <BFormInput 
                        id="input-password"
                        v-model="userRegister.password"
                        placeholder="пароль"
                        type="password"
                        class="form-control mb-3" 
                        required
                    />
                </BFormGroup>
                 
                <!-- Confirm Password -->
                <BFormGroup 
                    id="confirm-password"
                    label="Подтверждение пароля"
                    label-for="input-confirm-password"
                    >
                    
                    <BFormInput 
                        id="input-confirm-password"
                        v-model="userRegister.confirmPassword"
                        placeholder="подтверждение пароля"
                        type="password"
                        class="form-control mb-3"
                        required
                    />
                    <BFormText v-if="passwordError" text-variant="danger">
                        {{ passwordError }}
                    </BFormText>
                    <button :disabled="passwordError" type="submit" class="btn btn-primary w-100 mt-3">Регистрация</button>
                </BFormGroup>
            </BForm>
            <!-- LOGIN  -->
            <BForm @submit.prevent="signIn">
                
                <BFormGroup
                    id="bformgr-1"
                    label="Имя пользователя"
                    label-for="input-username"
                    class="mb-3">
                    
                    <BFormInput 
                        id="input-username"
                        v-model:="userLogin.username"
                        placeholder="введите имя пользователя"
                        required
                    />
                </BFormGroup>

                    <BFormGroup
                    id="bformgr-2"
                    label="Пароль"
                    label-for="input-password"
                    class="mb-3">
                        
                    <BFormInput 
                        id="input-password"
                        v-model:="userLogin.password"
                        placeholder="введите пароль"
                        type="password"
                        class="mb-2"
                        required
                    />
                    <BFormText v-if="badresponse" text-variant="danger">Неверное имя или пароль, попробуйте еще!</BFormText>
                    <button type="submit" class="btn btn-primary w-100 mt-3">Войти</button>
                </BFormGroup>
            </BForm>
            <!-- <BNav>
                <BButton v-if="!formCheck.showRegisterButton" class="mt-3" @click="formCheck.showSubRegisterForm=false;
                formCheck.showSubLoginForm=true; formCheck.showRegisterButton=true">Войти</BButton>
                <BButton v-if="formCheck.showRegisterButton" class="mt-3" @click="formCheck.showSubRegisterForm=true;
                formCheck.showSubLoginForm=false; formCheck.showRegisterButton=false">Регистрация</BButton>
            </BNav> -->
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
import { ref, reactive, watch, onMounted } from 'vue';
import SigninUsersService from '@/services/SigninUsersService';
import SignupUsersService from '@/services/SignupUsersService';
import RoutesService from '@/services/RoutesService';
import Datepicker from '@vuepic/vue-datepicker';
import {
  BButton,
  BDropdownItem,
  BForm,
  BFormGroup,
  BFormInput,
  BFormText,
  BInput,
  BModal,
  BNav,
  BNavbar,
  BNavbarBrand,
  BNavbarNav,
  BNavItemDropdown
} from 'bootstrap-vue-next';

export default {
  name: 'AppRoutes',
  components: {
    BButton,
    BDropdownItem,
    BForm,
    BFormGroup,
    BFormInput,
    BFormText,
    BInput,
    BModal,
    BNav,
    BNavbar,
    BNavbarBrand,
    BNavbarNav,
    BNavItemDropdown,
    Datepicker
  },
  setup() {

    // Реактивные данные с ref
    const userRegister = reactive({
      username: '',
      password: '',
      confirmPassword: ''
    });

    const userLogin = reactive({
      username: '',
      password: ''
    });

    const formCheck = reactive({
      showLoginForm: false,
      badresponse: false,
      showSubLoginForm: true,
      showSubRegisterForm: false,
      showRegisterButton: true,
      registerSuccess: false,
    });
    const isFadingOut = ref(false);
    const showPopup = ref(false);
    const popupMessage = ref('');
    const routes = ref([]);
    const date = ref(null);
    const arrivalDate = ref(null);
    const itemTransport = ref('');
    const passwordError = ref(null);

    // Методы
    const getRoutes = async () => {
      try {
        const response = await RoutesService.getRoutes();
        routes.value = response.data;
      } catch (error) {
        console.error('Failed to fetch routes:', error);
      }
    };

    const signup = async () => {
      if (passwordError.value) return;
      try {
        const response = await SignupUsersService.signupUser(userRegister.username, userRegister.password);
        sessionStorage.setItem('accessToken', response.data.accessToken);
        formCheck.registerSuccess = true;
        userRegister.username = '';
        userRegister.password = '';
        userRegister.confirmPassword = '';
      } catch (error) {
        console.error('Signup failed:', error.message);
      }
    };

    const signIn = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const response = await SigninUsersService.signinUser(userLogin.username, userLogin.password, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        sessionStorage.setItem('accessToken', response.data.accessToken);
        formCheck.badresponse = false;

        popupMessage.value = 'Регистрация успешна!';
        showPopup.value = true;

        setTimeout(() => {
          isFadingOut.value = true;
          setTimeout(() => {
            showPopup.value = false;
            isFadingOut.value = false;
          }, 500);
        }, 3000);

        formCheck.showLoginForm = false;
        userLogin.username = '';
        userLogin.password = '';

      } 
      catch (error) {
        console.error('Sign-in failed:', error.message);
        formCheck.badresponse = true;
      }
    };

    const selectTransport = (transport) => {
      itemTransport.value = transport;
    };

    watch(() => userRegister.confirmPassword, (newValue) => {
      if (newValue !== userRegister.password) {
        passwordError.value = "Пароли не совпадают!";
      } else {
        passwordError.value = null;
      }
    });

    onMounted(() => {
      getRoutes();
    });

    return {
      userRegister,
      userLogin,
      formCheck,
      routes,
      date,
      arrivalDate,
      itemTransport,
      passwordError,
      getRoutes,
      signup,
      signIn,
      selectTransport,
      showPopup,
      popupMessage,
      isFadingOut
    };
  }
};
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