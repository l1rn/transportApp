<template>
    <div>
        <input v-model="username" placeholder="Username"/>
        <input v-model="password" placeholder="Password" type="password"/>
        <button @click="signup">submit</button>
    </div>
</template>
<script>
import SignupUsersService from '@/services/SignupUsersService';
export default{
    name: 'AppUsersSignUp',
    data(){
        return{
            username: '',
            password: '',
        }
    },
    methods:{
        async signup(){
            try{
                const response = await SignupUsersService.signupUser(this.username, this.password);
                console.log(response.data.token);
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
    
</style>