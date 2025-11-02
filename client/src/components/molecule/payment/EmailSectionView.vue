<template>
  <div class="email-container">
    <div class="email-change-container">
        <a 
        v-if="hasEmail"
        @click="activateEmail">
            Хотите изменить email?
        </a>
        </div>
        <div class="email-change-form">
            <template
            v-if="!codeSent">
            <div class="input-block">
            <label for="email-input">
                Email
            </label>
                <input 
                id="email-input" 
                type="email" 
                v-model="emailSubmit.newEmail"
                :disabled="hasEmail && !isWantToChangeEmail">
            </div>
            <div class="input-block">
                <label for="email-input">
                    Email повторно (только ручной ввод):
                </label>
                <input 
                id="email-input" 
                type="email" 
                v-model="emailSubmit.confirmEmail"
                :disabled="hasEmail && !isWantToChangeEmail">
            </div>
            <div v-if="emailError">
                {{ emailError }}
            </div>
                <template v-if="!hasEmail">
                    <div class="button-block">
                        <button @click="setNewEmailRequest">
                            Подтвердить
                        </button>
                    </div>
                </template>
            </template>       
            <template v-else-if="codeSent">
                <input 
                type="text" 
                v-model="codeValue">
                <button @click="confirmNewEmail">
                    Подтвердить
                </button>
            </template>
        </div>
    </div>
</template>

<script setup lang="ts">
import notification from '@/shared/plugins/notifications';
import { userService } from '@/shared/services/userService';
import { OrderInfoResponse } from '@/shared/types/payment';
import { AxiosError } from 'axios';
import { onMounted, ref, watch } from 'vue';

const props = defineProps<OrderInfoResponse>();

const emailSubmit = ref({
    newEmail: '',
    confirmEmail: ''
})

const codeSent = ref<boolean>(false);
const codeValue = ref<string>("");

const hasEmail = ref<boolean>(false);
const isWantToChangeEmail = ref<boolean>(false);

const activateEmail = () => {
    if(!props.hasEmail) return;
    isWantToChangeEmail.value = true;
    hasEmail.value = false;
}

watch(
    () => [hasEmail.value = props.hasEmail],
    () => {
        if(props.hasEmail){
            hasEmail.value = props.hasEmail;
        }
    }
)

const emailError = ref<string | null>(null);

const setNewEmailRequest = async() => {
    if(emailError.value) {
        notification.error("Email не совпадают!");
        return;
    }
    try {
        await userService.requestUserEmail(emailSubmit.value.newEmail);
        codeSent.value = true;
        notification.success("Код с подтверждением отправлен на указанный вами адрес!");
        hasEmail.value = true;
        isWantToChangeEmail.value = false;
        emailSubmit.value.newEmail = '';
        emailSubmit.value.confirmEmail = '';
    }
    catch(e){
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на смену почты")
        console.log("status: " + axiosError.status);
    }
}

const confirmNewEmail = async() => {
    try{
        await userService.confirmUserEmail(codeValue.value);
        notification.success("Адрес был успешно изменен!");
        hasEmail.value = true;
        codeSent.value = false;
    }
    catch(e){
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на смену почты")
        console.log("status: " + axiosError.status);
    }
}

watch(
    () => [emailSubmit.value.newEmail, emailSubmit.value.confirmEmail],
    () => {
        if(emailSubmit.value.newEmail === '' &&
            emailSubmit.value.confirmEmail === '') 
            return;
        if(emailSubmit.value.newEmail !== emailSubmit.value.confirmEmail || 
            emailSubmit.value.confirmEmail === '')
            emailError.value = "Email не совпадают!";
        else
            emailError.value = null;
    }
)
</script>

<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.email-container {
    @include mixins.display-column();
    width: 50%;
    gap: 0.75rem;
    .email-change-container{
        a {
            @include mixins.link-underline(colors.$dark-grey);
        }
    }
    .email-change-form{
        @include mixins.display-column();
        gap: 0.5rem;
        .input-block {
        text-transform: uppercase;
        letter-spacing: 0.03rem;
        font-size: 0.75rem;
        font-weight: 800;
        @include mixins.display-column();
        gap: 0.25rem;
            input {
                @include mixins.custom-input();
            }
        }
    }
}

</style>