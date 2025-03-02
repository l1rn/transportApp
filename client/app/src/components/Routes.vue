<!-- eslint-disable vue/multi-word-component-names -->
<template>
    <div class="header-container-custom">
        <div class="navbar-custom fixed-top">
            <BNavbar>
                <BNavbarBrand class="brand" href="/#">ololotravel</BNavbarBrand>
                
                <BNavbarNav>
                    <BNavItemDropdown v-model="textProfile" text="👤Профиль">
                        <BNav align="center">
                            <BDropdownItem href="/auth/profile">Мои заказы</BDropdownItem>
                            <BDropdownItem @click="formCheck.showLoginForm = true">Авторизация</BDropdownItem>
                            <BDropdownItem href="/auth/logout">Выйти</BDropdownItem>
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

        <BModal v-model="formCheck.showLoginForm" title="Регистрация профиля" class="xl" hide-footer>
            <BForm v-if="formCheck.showSubRegisterForm" @submit.prevent="signup">
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
                 
                <!-- Password -->
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
                    <BFormText v-if="formCheck.passwordError" text-variant="danger">
                        {{ formCheck.passwordError }}
                    </BFormText>
                    <button :disabled="formCheck.passwordError" type="submit" class="btn btn-primary w-100 mt-3">Регистрация</button>
                </BFormGroup>
            </BForm>
            <BForm v-if="formCheck.showSubLoginForm" @submit.prevent="signIn">
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
            <BNav>
                <BButton v-if="!formCheck.showRegisterButton" class="mt-3" @click="formCheck.showSubRegisterForm=false;
                formCheck.showSubLoginForm=true; formCheck.showRegisterButton=true">Войти</BButton>
                <BButton v-if="formCheck.showRegisterButton" class="mt-3" @click="formCheck.showSubRegisterForm=true;
                formCheck.showSubLoginForm=false; formCheck.showRegisterButton=false">Регистрация</BButton>
            </BNav>
        </BModal>
    <div class="main-container">
        <div class="container-route-to-search">
            <BImg>12123</BImg>
        </div>
        <div class="container-to-see-all-routes">
            
        </div>
    </div>
</template>
<script>
import SigninUsersService from '@/services/SigninUsersService';
import SignupUsersService from '@/services/SignupUsersService';
import RoutesService from '@/services/RoutesService';
import BookingService from '@/services/BookingService'
import Datepicker from '@vuepic/vue-datepicker';


import {  BButton, BDropdownItem, BForm, BFormGroup, BFormInput, BFormText,
     BInput, BModal, BNav, BNavbar, BNavbarBrand, BNavbarNav, BNavItemDropdown,
     } from 'bootstrap-vue-next';
export default {
    name: 'AppRoutes',
    components:{
        BNavbar,
        BNavbarBrand,
        BNavbarNav,
        BNavItemDropdown,
        BDropdownItem,
        BModal,
        BForm,
        BFormGroup,
        BFormInput,
        BFormText,
        BButton,
        BInput,
        Datepicker,
        BNav
    },
    data(){
        return{
            userRegister: 
            { 
                username: '', 
                password: '', 
                confirmPassword: ''
            },
            userLogin: 
            {
                username: '', 
                password: ''
            },
            formCheck:
            {
                passwordError: null,
                showLoginForm: false,
                badresponse: false,
                showSubLoginForm: true,
                showSubRegisterForm: false,
                showRegisterButton: true,
                loginSuccess: false,
                registerSuccess: false,
                textLoginSuccess: '',
            },
                routes: [],
                date: null,
                arrivalDate: null,
                itemTransport: '',
        }
    },
    methods:{
        getRoutes(){
            RoutesService.getRoutes().then((response) =>{
                this.routes = response.data
            });
        },

        async signup(){
            try{
                if(!this.formCheck.passwordError){
                    const response = await SignupUsersService.signupUser(this.userRegister.username, this.userRegister.password);
                    console.log(response.data.token);
                    localStorage.setItem('token', response.data.token);
                    this.registerSuccess = true;
                    this.userRegister.username = '';
                    this.userRegister.password = '';
                    this.userRegister.confirmPassword = '';
                }
                
            }
            catch(error){
                console.log(error.message);
            }
        },
        async signIn(){
            try{
                const token = localStorage.getItem('token');
                const response = await SigninUsersService.signinUser(this.userLogin.username, this.userLogin.password, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
                localStorage.setItem('token', response.data.token);
                this.formCheck.badresponse = false;
                this.formCheck.loginSuccess = true;
                this.userLogin.username = '';
                this.userLogin.password = '';
            }
            catch(error){
                console.log(error.message);
                this.formCheck.loginSuccess = false;
                this.badresponse = true;
            }
        },
        async addbook(){
            try{
                console.log(localStorage.getItem("token"));

                const token = localStorage.getItem('token');
                const response = await BookingService.addBooking(this.routeId, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
                this.$router.push('/auth/profile');
            }
            catch(error){
                
                this.$router.push('/auth/sign-up');
                console.log(error.message);
            } 
        },
        selectTransport(transport){
            this.itemTransport = transport;
        }
    },
    watch: {
        "user.confirmPassword"(newValue){
            if(newValue !== this.user.password){
                this.formCheck.passwordError = "Пароли не совпадают!";
            }
            else{
                this.formCheck.passwordError = null;
            }
        }
    },
    computed:{
        
    },
    created(){
        this.getRoutes();
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
    height: 200vh;
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

</style>