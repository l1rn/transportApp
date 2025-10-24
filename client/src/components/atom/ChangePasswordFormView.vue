<template>
    <div 
    class="change-password-form">
        <form @submit.prevent="changePassword">
            <div 
            class="form-wrapper"  
            ref="formRef">
                <label class="form-title">СМЕНА ПАРОЛЯ</label>
                <div class="input-block">
                    <label for="old">
                        Старый пароль
                    </label>
                    <input 
                    id="old" 
                    v-model="passwordRequest.oldPassword" 
                    type="password" 
                    placeholder="старый пароль"
                    required>
                </div>
                <div class="input-block">
                    <label for="new">
                        Новый пароль
                    </label>
                    <input 
                    id="new" 
                    v-model="passwordRequest.newPassword" 
                    type="password"
                    placeholder="новый пароль"
                    required>
                </div>
                <div class="input-block">
                    <label for="passwordConfirm">
                        Подтвердите пароль
                    </label>
                    <input 
                    id="passwordConfirm" 
                    v-model="confirmPassword" 
                    type="password"
                    placeholder="подтвердите пароль" 
                    required>
                    <div 
                    v-if="!doPasswordMatch" 
                    class="password-do-not-match">
                        Пароли не совпадают
                    </div>
                </div>
                <div class="button-block">
                    <button 
                    type="submit" 
                    class="submit-button" 
                    :disabled="!doPasswordMatch"
                        :class="{ 'disabled': !doPasswordMatch }">
                        Подтвердить
                    </button>
                </div>
            </div>
        </form>
    </div>
</template>
<script setup lang="ts">
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { userService } from '@/services/userService';
import { useModalStore } from '@/stores/useModalStore';
import { computed, ref } from 'vue';

const formRef = ref<HTMLElement | null>(null);
const modalStore = useModalStore();

const passwordRequest = ref({
    oldPassword: '',
    newPassword: ''
})

const doPasswordMatch = computed(() => {
    return passwordRequest.value.newPassword === confirmPassword.value
})

const confirmPassword = ref('')

const changePassword = async () => {
    if (!doPasswordMatch.value) return;
    try {
        await userService.changeUserPassword(passwordRequest.value);
        passwordRequest.value.oldPassword = ''
        passwordRequest.value.newPassword = ''
        confirmPassword.value = ''
    } catch (e) {
        console.log(e)
        passwordRequest.value.oldPassword = ''
        passwordRequest.value.newPassword = ''
        confirmPassword.value = ''
    }
}

useConditionalClickOutside(
    formRef,
    () => modalStore.isOpen('change-password-form'),
    () => modalStore.close('change-password-form')
)
</script>
<style scoped lang="scss">
@import "../../assets/styles/static/mixin.d.scss";
@import "../../assets/styles/static/color.d.scss";


.change-password-form {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 95%;
    
    form {
        width: 80%;
    }

    .form-wrapper {
        background: $white;
        display: flex;
        flex-direction: column;
        gap: 1rem;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);
        border-radius: 16px;
        padding: 1.5rem 1rem;

        .form-title {
            display: flex;
            color: #1d1d1d;
            font-size: 26px;
            justify-content: center;
        }

        input {
            @include custom-input();
        }

        .input-block {
            display: flex;
            flex-direction: column
        }

        .submit-button {
            @include button-clear(#163570, #ffffff);
            font-size: 20px;
            padding: 0.7rem 1rem;
            border-radius: 8px;
            transition: all 0.4s;
            box-shadow: 0 0 8px #16357040;
            width: 100%;

            &:hover:not(:disabled) {
                background: #0f409b;
                transform: translateY(-1px);
                box-shadow: 0 0 16px #16357080;
            }

            &:disabled {
                background: #444;
                cursor: not-allowed;
                box-shadow: none;
            }
        }
    }
}
</style>