<template>
    <div>
        <input v-model="username">
        <input v-model="password" type="password">
        <button @click="signIn">submit</button>
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
<style lang="">
    
</style>