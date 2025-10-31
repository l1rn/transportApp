<template>
    <div 
        v-if="modalStore.isOpen('payment-confirm-code-form')"
        class="code-confirmation">
            <ModalFormView
                v-bind="modalsConfig['payment-confirm-code-form']"
                v-model="codeValue"    
            />
        </div>
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
        
        <template>
            <div class="code-confirmation-container">
                <input type="text">
                <button>Подтвердить</button>
            </div>
        </template>
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
import codeIcon from "../../../assets/icons/lock.svg";
import InputSuggestionView from '@/components/atom/InputSuggestionView.vue';
import { useRequestHandler } from '@/composable/useRequestHandler';
import notification from '@/plugins/notifications';
import { paymentService } from '@/services/paymentService';
import { useModalStore } from '@/stores/useModalStore';
import { ModalPropsView } from '@/types/component';
import { OrderInfoResponse } from '@/types/payment';
import { AxiosError } from 'axios';
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ModalFormView from "@/components/atom/ModalFormView.vue";

const modalStore = useModalStore();

const router = useRouter();
const orderData = ref<OrderInfoResponse>();
const codeValue = ref<string>("");  

onMounted(async () => {
    if (bookingId === null) return;
    const response = await paymentService.getOrderInfo(Number(bookingId));
    orderData.value = response.data;
    paymentMethods.value = orderData.value?.paymentMethods;

    const routeTitle = router.currentRoute.value.meta.title as string;
    if(routeTitle){
        document.title = routeTitle;
    }
})

const submitPaymentCodeConfirmation = async(): Promise<void> => {
    useRequestHandler().handleConfirm(
        () => paymentService.confirmPayment(
            modalsConfig['payment-confirm-code-form'].externalId, 
            codeValue.value
        ),
        "Ваш платеж был подтвержден!",
        "payment-confirm-code-form",
        codeValue
    )
}

const modalsConfig: Record<string, ModalPropsView> = {
'payment-confirm-code-form': {
    icon: codeIcon,
    title: "Введите код подтверждения",
    desc: `Введите код, чтобы привязать новый адрес электронной почты к вашему аккаунту. 
    Для подтверждения, введите этот код снизу. Код был отправлен на указанный вами email`,
    buttonName: "Подтвердить",
    model: codeValue,
    inputPlaceholder: "123456",
    inputType:"text",
    storeKey: "payment-confirm-code-form",
    submitFunc: submitPaymentCodeConfirmation,
    externalId: ""
  }
}

const route = useRoute();

const bookingId = route.query.bookingId;

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
        modalsConfig['payment-confirm-code-form'].externalId = response.data;
        modalStore.open('payment-confirm-code-form');
    }
    catch (e) {
        const axiosError = e as AxiosError;
        notification.error("Не удалось отправить запрос на оплату!: " + axiosError.message);
        console.log(axiosError.status);
    }
}
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