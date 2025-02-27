<template>
    <div class="container mt-5">
        <div class="card shadow-lg p-4 custom-form">
            <h2 class="text-center mb-4">Авторизация</h2>
            <form>
                <!-- User -->
                <div class="mb-3">
                    <label class="form-label">Имя пользователя</label>
                    <input class="form-control" v-model="username" placeholder="Username"/>
                </div>
                <!-- Pwd -->

                <div class="mb-3">
                    <label class="form-label">Пароль</label>
                    <input class="form-control" v-model="password" placeholder="Password" type="password" required/>
                </div>

                <!-- Submit -->

                <button class="btn btn-primary w-100" @click="signin">Войти</button>
            </form>
        </div>
    </div>
</template>
<script>
import SigninUserService from '@/services/SigninUsersService'
export default {
    name: "AppUsersSignIn",
    data(){
        return{
        username: '',
        password: ''
        }
    },
    methods:{
        async signIn(){
            try{
                const token = localStorage.getItem('token');
                const response = await SigninUserService.signinUser(this.username, this.password, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
                localStorage.setItem('token', response.data.token);
            }
            catch(error){
                console.log(error.message);
            }
        }     
    } 
}
</script>
<style>

.btn-primary {
    background-color: #eb811e;
    border: none;
    color:rgb(255, 255, 255);
}
</style>