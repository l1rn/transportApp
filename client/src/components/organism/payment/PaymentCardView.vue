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
            <a @click="cancelPayment()">
                Отказаться от оплаты и вернуться на главную
            </a>
        </div>
        <template v-if="isCodeSent">
            <div class="code-confirmation">
                <div>Введите код</div>
                <div class="input-block">
                    <input 
                    v-model="confirmationCode"
                    type="text">
                    <button @click="confirmPayment">
                        Подтвердить
                    </button>
                    <a @click="resendCode(externalId)">Отправить код повторно</a>
                </div>
            </div>
        </template>
    </div>
</template>
<script setup lang="ts">
import EmailSectionView from "@/components/molecule/payment/EmailSectionView.vue";
import InputSuggestionView from '@/components/atom/InputSuggestionView.vue';
import notification from '@/shared/plugins/notifications';
import { paymentService } from '@/shared/services/paymentService';
import { AxiosError, HttpStatusCode } from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { OrderInfoResponse } from "@/shared/types/payment";
import { usePaymentNavigation } from "@/composable/usePaymentNavigation";

const route = useRoute();
const router = useRouter();

const orderData = ref<OrderInfoResponse>();

const paymentMethod = ref<string>("SIMULATION");
const paymentMethods = ref<Array<string>>();

const externalId = ref<string | null>(null);
const isCodeSent = ref(false);
const confirmationCode = ref<string | null>(null);

const createPayment = async () => {
    if (!paymentMethod.value) {
        notification.error("Заполните способ оплаты");
        return;
    }
    try {
        const response = await paymentService.createPayment(
            Number(route.query.bookingId),
            paymentMethod.value
        );
        notification.success('Ваша заявка была создана! Код был отправлен вам на почту')
        isCodeSent.value = true;
    }
    catch (e) {
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на оплату!: " + axiosError.message);
        console.log(axiosError.status);
    }
}

const confirmPayment = async() => {
    if (!confirmationCode.value) {
        notification.error("Заполните поле с кодом");
        return;
    }
    try {
        await paymentService.confirmPayment(
            externalId.value,
            confirmationCode.value
        );
        notification.success("Ваш заказ успешно подтвержден, проверьте почту!");
        confirmationCode.value = "";
        isCodeSent.value = false;
        redirectToMyPayments();
    }
    catch (e) {
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на оплату!: " + axiosError.message);
        console.log(axiosError.status);
    }
}

const cancelPayment = async() => {
    if(!localStorage.getItem('externalId')){
        return;
    }
    try{
        await paymentService.cancelPayment(externalId.value);
        notification.success("Заказ был успешно отменен!");
        router.push("/home");
    }
    catch(e){
        const axiosError = e as AxiosError;
        console.log(axiosError.code);
    }
}

const redirectToMyPayments = () => {
    checkPaymentStatus(true);
    setView('my-payments');
    return;
}

const { setView, checkPaymentStatus } = usePaymentNavigation();

onMounted(async() => {
    await initializePayment();
})

const initializePayment = async() => {
    const bookingId = route.query.bookingId;
    if(!bookingId){
        redirectToMyPayments()
    }

    try {
        const info = await paymentService.getOrderInfo(Number(bookingId));
        if(info.data.paid) {
            notification.success("Ваш заказ был уже оплачен!");
            redirectToMyPayments();
        }
        if(info.data.inProgress) {
            const response = await paymentService.getExternalId(Number(bookingId));
            isCodeSent.value = true;
            externalId.value = response.data;
        }
        orderData.value = info.data;
        paymentMethods.value = info.data.paymentMethods;
    }
    catch(e) {
        const axiosError = e as AxiosError;
        if(axiosError.status === HttpStatusCode.NotFound || axiosError.status === HttpStatusCode.BadRequest){
            notification.error("Этого букинга нету в вашем профиле!");
            redirectToMyPayments();
            return;
        }
        if(axiosError.status === HttpStatusCode.Forbidden) {
            notification.error("Для доступа на эту страницу, нужно авторизоваться");
            router.push("/home");
            return;
        }
    }
}

const resendCode = async(externalId: string | null) => {
    if(!externalId) return;
    try {
        await paymentService.resendConfirmationCode(externalId);
        notification.success("Новый код подтверждения был отправлен на почту!");
    }
    catch(e){
        notification.error("Не удалось отправить новый код подтверждения вам на почту!");
    }
}
</script>

<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.payment-container {
    @include mixins.display-column();
    width: 80%;
    background: colors.$light-grey;
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
    .code-confirmation{
        @include mixins.display-column();
        gap: 0.25rem;
        justify-content: center;
        .input-block {
            display: flex;
            gap: 0.5rem;
        }
        input{
            @include mixins.custom-input();
        }
        button{
            @include mixins.button-clear(colors.$medium-green, colors.$white);
            padding: 0.5rem 1rem;
            border-radius: 8px;
            font-size: 1.05rem;
            margin-right: 2rem;
        }
        a {
            @include mixins.display-center();
            color: #3d7de5;
            cursor: pointer;
            &:hover {
                text-decoration: underline;
            }
        }
    }
}
</style>