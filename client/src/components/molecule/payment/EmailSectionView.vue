<template>
  <div class="email-container">
    <div class="email-change-container">
        <a @click="activateEmail">
            Хотите изменить email?
        </a>
        </div>
        <div class="email-change-form">
            <div class="input-block">
            <label for="email-input">
                Email
            </label>
                <input 
                id="email-input" 
                type="email" 
                v-model="emailSubmit.newEmail"
                :disabled="hasEmail">
            </div>
            <div class="input-block">
                <label for="email-input">
                    Email повторно (только ручной ввод):
                </label>
                <input 
                id="email-input" 
                type="email" 
                v-model="emailSubmit.confirmEmail"
                :disabled="hasEmail">
            </div>
            <div v-if="emailError">
                {{ emailError }}
            </div>
                <template v-if="!hasEmail">
                <div class="button-block">
                    <button>Подтвердить</button>
                </div>
            </template>
        </div>
    </div>
</template>

<script setup lang="ts">
import { OrderInfoResponse } from '@/types/payment';
import { onMounted, ref, watch } from 'vue';

const props = defineProps<OrderInfoResponse>();
const hasEmail = ref<boolean>();

const emailSubmit = ref({
    newEmail: '',
    confirmEmail: ''
})

const activateEmail = () => {
    if(!props.hasEmail) return;
    hasEmail.value = true;
}

onMounted(() => hasEmail.value = props.hasEmail);

const emailError = ref<string | null>(null);

watch(
    () => [emailSubmit.value.newEmail, emailSubmit.value.confirmEmail],
    () => {
        if(emailSubmit.value.newEmail !== emailSubmit.value.confirmEmail)
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