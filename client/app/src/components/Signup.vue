<template>
    <div class="container mt-5">
        <div class="card shadow-lg p-4 custom-form">
            <h2 class="text-center mb-4">Регистрация</h2>
            <form>
                <!-- User -->
                <div class="mb-3">
                    <label class="form-label">Имя пользователя</label>
                    <input class="form-control" v-model="user.username" placeholder="пользователь"/>
                </div>
                <!-- Pwd -->

                <div class="mb-3">
                    <label class="form-label">Пароль</label>
                    <input class="form-control" v-model="user.password" placeholder="пароль" type="password" required/>
                </div>

                <!-- Confirm -->
                <div class="mb-3">
                    <label class="form-label">Подтверждение пароля</label>
                    <input class="form-control" v-model="user.confirmPassword" placeholder="подтверждение пароля" required type="password">
                    <p v-if="passwordError" class="text-danger" mt-1>{{ passwordError }}</p>
                </div>
                <!-- Submit -->

                <button class="btn btn-primary w-100" @click="signup" :disabled="passwordError">Зарегистрироваться</button>
            </form>
        </div>
    </div>
</template>
<script>
import SignupUsersService from '@/services/SignupUsersService';
export default{
    name: 'AppUsersSignUp',
    data(){
        return{
            user:{
                username: '',
                password: '',
                confirmPassword: '',
            },
            passwordError: null,
        };
    },
    methods:{
        async signup(){
            try{
                if(!this.passwordError){
                    const response = await SignupUsersService.signupUser(this.user.username, this.user.password);
                    console.log(response.data.token);
                    localStorage.setItem('token', response.data.token);
                }
                
            }
            catch(error){
                console.log(error.message);
            }
        }
    },
    watch: {
        "user.confirmPassword"(newValue){
            if(newValue !== this.user.password){
                this.passwordError = "Пароли не совпадают!";
            }
            else{
                this.passwordError = null;
            }
        }
    }
}
</script>
<style>
    #app{
        font-family: Montserrat;
    }
.btn-primary {
    background-color: #ffc400;
    border: none;
    color:rgb(255, 255, 255);
}
</style>