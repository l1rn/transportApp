<template>
    <div class="container mt-5">
        <div class="card shadow-lg p-4 custom-form">
            <h2 class="text-center mb-4">Авторизация</h2>
            <BForm @submit.prevent="signIn">
                <BFormGroup
                    id="bformgr-1"
                    label="Имя пользователя"
                    label-for="input-username"
                    class="mb-3">
                    
                    <BFormInput 
                        id="input-username"
                        v-model:="username"
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
                        v-model:="user.password"
                        placeholder="введите пароль"
                        type="password"
                        class="mb-2"
                        required
                    />
                    <BFormText v-if="badresponse" text-variant="danger">Неверное имя или пароль, попробуйте еще!</BFormText>
                    <button type="submit" class="btn btn-primary w-100 mt-3">Авторизация</button>
                </BFormGroup>
            </BForm>
        </div>
    </div>
</template>
<script>
import SigninUserService from '@/services/SigninUsersService'
import { BForm, BFormGroup, BFormInput, BFormText } from 'bootstrap-vue-next';

export default {
    name: "sign-up",
    components:{
        BForm,
        BFormGroup,
        BFormInput,
        BFormText
    },
    data(){
        return{
            user:{
                username: '',
                password: ''
            },
            badresponse: false,
        }
    },
    methods:{
        async signIn(){
            try{
                const token = sessionStorage.getItem('token');
                const response = await SigninUserService.signinUser(this.username, this.password, {
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });
                console.log(response.data);
                sessionStorage.setItem('token', response.data.token);
                this.badresponse = false;
            }
            catch(error){
                console.log(error.message);
                this.badresponse = true;
            }
        }     
    } 
}
</script>
<style scoped>
.container{
    font-family: Montserrat;
}
.btn-primary {
    font-family: Montserrat;
    font-size: large;
    background-color: #089754;
    border: none;
    color:#f7d206;
    transition: transform 0.2s ease-in, background-color 0.2s ease-in-out, color 0.2s ease;
}

.btn-primary:hover{
    transform: scale(1.01) translateY(1px);
}
</style>