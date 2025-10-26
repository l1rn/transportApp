<template>
    <div class="email-form">
        <div 
        ref="formRef"
        class="email-form-container">
            <div class="text-container">
                <div class="icon">
                    <img 
                    src="../../assets/icons/mail.svg" 
                    alt="email">
                </div>
                <div class="title">
                    Укажите email
                </div>
                <div class="desc">
                    Введите адрес электронной почты, чтобы иметь возможность восстановить доступ к аккаунту.
                    Для подтверждения, вам будет отправлен код на вашу почту
                </div>
            </div>
            <div class="input-container">
                <input 
                type="email"
                v-model="newEmail"
                placeholder="example@exaple.com"
                >
                <button @click="submitEmailRequest">
                    Отправить код
                </button>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { userService } from '@/services/userService';
import { useModalStore } from '@/stores/useModalStore';
import { AxiosError } from 'axios';
import { ref } from 'vue';

const newEmail = ref<string>("");

const formRef = ref<HTMLElement | null>(null);
const modalStore = useModalStore();

const submitEmailRequest = async() => {
    try{
        const response = await userService.requestUserEmail(newEmail.value);
        console.log(response.data)
    }
    catch(e){
        const axiosError = e as AxiosError;
        console.log(axiosError.status);
    }
}

useConditionalClickOutside(
    formRef,
    () => modalStore.isOpen('change-email-form'),
    () => modalStore.close('change-email-form')
)
</script>
<style scoped lang="scss">
@import "../../assets/styles/static/color.d.scss";
@import "../../assets/styles/static/mixin.d.scss";

.email-form {
    position: relative;
    @include display-center();
    height: 100%;
    .email-form-container{
        background: $white;
        width: 80%;
        max-width: 700px;
        max-height: 500px;
        height: 100%;
        @include display-column(center, center);
        border-radius: 24px;
        gap: 2rem;
        .text-container{
            @include display-column(center, center);
            text-align: center;
            gap: 0.75rem;
            .icon{
                img{
                    width: 56px;
                }
            }
            .title{
                font-size: 24px;
            }
            .desc{
                width: 80%;
                font-size: 18px;
            }
        }
        .input-container {
            @include display-column();
            gap: 1.25rem;
            input {
                @include custom-input();
            }  
            button {
                @include button-clear($primary-blue, $white);
                padding: 0.75rem 1.25rem;
                border-radius: 8px;
                font-size: 1.15rem;
            }
        }
    }
}
</style>