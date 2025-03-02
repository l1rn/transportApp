<!-- eslint-disable vue/multi-word-component-names -->
<template>
   
    <div class="container">
        <BNavbar class="p-fixed">
            <BNavbarBrand class="brand" href="/routes">ololotravel</BNavbarBrand>
            <BNavbarNav>
                    <BNavItemDropdown v-model="textProfile" text="Профиль" right>
                    <BDropdownItem href="/auth/profile">Мои заказы</BDropdownItem>
                    <BDropdownItem @click="formCheck.showLoginForm = true">Авторизация</BDropdownItem>
                    <BDropdownItem href="/auth/logout">Выйти</BDropdownItem>
                </BNavItemDropdown>
            </BNavbarNav>
        </BNavbar>
        <div class="header-container">
            <h1 class="text-align-center">Здесь самые дешевые билеты</h1>
            <div class="d-flex gap-2">
                <BInput placeholder="Откуда"></BInput>
                <BInput placeholder="Куда"></BInput>
                <Datepicker v-model="date"
                placeholder="Когда"
                :format="'dd-MM-yyyy'"
                :dark="false"
                :enable-time-picker="false" />
                <Datepicker v-model="arrivalDate"
                placeholder="Обратно"
                :format="'dd-MM-yyyy'"
                :enable-time-picker="false" />
            </div>
            <div class="d-flex gap-4"></div>
        </div>
        <table class="table table-striped">
            <thead>
                <th>откуда</th>
                <th>куда</th>
                <th>время посадки</th>
                <th>время прибытия</th>
                <th>свободных мест</th>
            </thead>
            <tbody>
                 <tr v-for="route in routes" :key="route.id">
                    <td>{{ route.routeFrom }}</td> 
                    <td>{{ route.routeTo }}</td>
                    <td>{{ route.time }}</td>
                    <td>{{ route.arrivalTime }}</td>
                    <td>{{ route.availableSeats }}</td>
                    <button @click="addbook()">book</button>
                 </tr>   
            </tbody>
        </table>
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
    </div>

</template>
<script>
import SigninUsersService from '@/services/SigninUsersService';
import SignupUsersService from '@/services/SignupUsersService';
import RoutesService from '@/services/RoutesService';
import BookingService from '@/services/BookingService'
import Datepicker from '@vuepic/vue-datepicker';


import {  BButton, BDropdownItem, BForm, BFormGroup, BFormInput, BFormText,
     BInput, BModal, BNavbar, BNavbarBrand, BNavbarNav, BNavItemDropdown,
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
        Datepicker
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
            },
                textProfile: 'Профиль',
                routes: [],
                date: null,
                arrivalDate: null,
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
                this.userLogin.username = '';
                this.userLogin.password = '';
            }
            catch(error){
                console.log(error.message);
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
.container{
    font-family: Montserrat;
}
.fade-alert {
  animation: fadeOut 4s ease-in-out;
}

@keyframes fadeOut {
  0% { opacity: 1; }
  100% { opacity: 0; }
}
.brand{
    color: rgb(27, 27, 27);
    font-size: large;
}
</style>