<template>
    <div class="container mt-5">
        <div class="card shadow-lg p-4 custom-form">
            <h2 class="text-center mb-4">Регистрация</h2>
            <BForm @submit.prevent="">
                <!-- User -->
                <BFormGroup 
                    id="username"
                    label="Имя пользователя"
                    label-for="input-username"
                    class="mb-3">
                    
                    <BFormInput 
                        id="input-username"
                        v-model:model-value="user.username"
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
                        v-model="user.password"
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
                        v-model="user.confirmPassword"
                        placeholder="подтверждение пароля"
                        type="password"
                        class="form-control mb-3"
                        required
                    />
                    <BFormText v-if="passwordError" text-variant="danger">
                        {{ passwordError }}
                    </BFormText>
                    <button :disabled="passwordError" @click="signup" type="submit" class="btn btn-primary w-100 mt-3">Регистрация</button>
                </BFormGroup>
            </BForm>
        </div>
    </div>
</template>
<script>
import { BForm, BFormGroup, BFormInput, BFormText } from 'bootstrap-vue-next';
import SignupUsersService from "@/services/SignupUsersService";
export default{
    name: 'sign-up',
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
                password: '',
                confirmPassword: '',
            },
            success: false,
            isLoading: false,
            passwordError: null,
        };
    },
    
    methods:{
        async signup(){
            if(this.passwordError) return;
            try{
                this.isLoading = true;

                const userData = {
                  username: this.user.username,
                  password: this.user.password
                };
                const response = await SignupUsersService.signupUser(userData);
                console.log(response);
                this.$emit('registered', {
                  success: true,
                  message: '✅ Успешная регистрация!',
                });
                this.resetForm();
            }
            catch(error){
              this.$emit('registered', {
                success: false,
                message: error.response?.data?.message || error.message || 'Ошибка регистрации'
              });
            }
            finally {
              this.isLoading = false;
            }
        },
      resetForm(){
        this.user = {
          username: '',
          password: '',
          confirmPassword: '',
        }
      }
    },
    
    watch: {
        "user.confirmPassword"(newValue){
            if(newValue !== this.user.password || this.user.confirmPassword === ''){
                this.passwordError = "Пароли не совпадают!";
            }
            else{
                this.passwordError = null;
            }
        },
    }
}
</script>
<style scoped>
.container{
    font-family: Montserrat;
}
.b-group{
    color:red;
}
.btn-primary {
    font-family: Montserrat;
    font-size: large;
    background-color: #970838;
    border: none;
    color:#f7d206;
    transition: transform 0.2s ease-in, background-color 0.2s ease-in-out, color 0.2s ease;
}

.btn-primary:hover{
    transform: scale(1.01) translateY(1px);
}
</style>


