<template>
    <div class="payment-container">
        <div class="header-container">
            <div class="info-container">
                <div class="title">
                    Товар: {{ orderData?.orderFullName }}
                </div>
                <div class="price">
                    <span>
                        {{ orderData?.price }}.00
                    </span>
                    RUB
                </div>
            </div>
            <div class="line-container">
                <!-- to-do -->
                <!-- descriptive-button -->
            </div>
            <div class="payment-method-container">
                <div class="label">
                    выбор способа оплаты:
                </div>
                <div class="input">
                    <InputSuggestionView
                    v-model="paymentMethod"
                    type="select"
                    placeholder="Способ оплаты"
                    array-type="self"
                    :suggestion-list="paymentMethods"
                />
                </div>
            </div>
        </div>
        <EmailSectionView 
            v-bind="orderData!"
        />
        
        <div class="button-container">
            <button @click.stop="createPayment();">
                Оплатить
            </button>
            <a @click="router.replace('/home')">
                Отказаться от оплаты и вернуться на главную
            </a>
        </div>
    </div>
</template>
<script setup lang="ts">
import EmailSectionView from "@/components/molecule/payment/EmailSectionView.vue";
import InputSuggestionView from '@/components/atom/InputSuggestionView.vue';
import notification from '@/shared/plugins/notifications';
import { paymentService } from '@/shared/services/paymentService';
import { useModalStore } from '@/shared/stores/useModalStore';
import { AxiosError } from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { OrderInfoResponse } from "@/shared/types/payment";

const modalStore = useModalStore();

const route = useRoute();
const router = useRouter();

const bookingId = route.query.bookingId;
const orderData = ref<OrderInfoResponse>();

const paymentMethod = ref<string>("SIMULATION");
const paymentMethods = ref<Array<string>>();

const createPayment = async () => {
    if (!paymentMethod.value) {
        notification.error("Заполните способ оплаты");
        return;
    }
    try {
        const response = await paymentService.createPayment(
            Number(bookingId),
            paymentMethod.value
        );
        modalStore.open('payment-confirm-code-form');
    }
    catch (e) {
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на оплату!: " + axiosError.message);
        console.log(axiosError.status);
    }
}

onMounted(async() => {
    try{
        const info = await paymentService.getOrderInfo(Number(bookingId));
        orderData.value = info.data;
    }
    catch(e){
        const axiosError = e as AxiosError;
        if(axiosError.status === 403) {
            notification.error("Для доступа на эту страницу, нужно авторизоваться");
            router.push("/home");
            return;
        }
    }
})
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.payment-container {
    @include mixins.display-column();
    width: 80%;
    background: colors.$light-gray;
    max-width: 1024px;
    padding: 2rem 2rem 1.25rem;
    margin-top: 2rem;
    border-radius: 16px;
    gap: 1.5rem;

    .header-container {
        @include mixins.display-column();
        align-items: start;
        gap: 1rem;

        .info-container {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .title {
                font-size: 1.5rem;
                text-indent: 1rem;
            }

            .price {
                font-weight: 700;
                font-size: 1.25rem;
                span{
                    color: colors.$light-orange;
                }
            }
        }

        .line-container {
            height: 1px;
            background: #000;
            width: 100%;

        }
    }

    .payment-method-container {
        @include mixins.display-column();
        gap: 0.25rem;
        .label {
            text-transform: uppercase;
            font-size: 1.15rem;
            font-weight: 600;
        }
        .input{ 
            max-width: 100%;
            input{
                width: 100%;
            }
        }
    }

    .code-confirmation-container {

    }
    .button-container{
        display: flex;
        align-items: end;
        justify-content: space-between;
        button{
            @include mixins.button-clear(
                $back: #3d7de5,
                $color: colors.$white
            );
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            font-size: 1.1rem;
            border-radius: 8px;
            transition: 0.3s background ease;
            &:hover{
                background: colors.$darken-blue;
            }
        }
        a{
            @include mixins.link-underline();
        }
    }
}
</style>