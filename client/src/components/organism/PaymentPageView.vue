<template>
    <div class="payment-wrapper">
        <div 
        v-if="modalStore.isOpen('payment-confirm-code-form')"
        class="code-confirmation">
            <ModalFormView 
                v-bind="modalsConfig['payment-confirm-code-form']"
                v-model="codeValue"    
            />
        </div>
        <HeaderPaymentView />
        <div class="payment-container">
            <div class="header-container">
                <div class="info-container">
                    <div class="title">
                        Товар: {{ orderData?.orderFullName }}
                    </div>
                    <div class="price">
                        {{ orderData?.price }} Р
                    </div>
                </div>
                <div class="line-container"></div>
                <div class="payment-method-container">
                    <div class="label">
                        выбор способа оплаты:
                    </div>
                    <input v-model="paymentMethod" type="text">
                    <template v-if="orderData?.paymentMethods">
                        <div v-for="method in orderData.paymentMethods" :key="method">
                            {{ method }}
                        </div>
                    </template>
                </div>
            </div>

            <div class="email-container">
                <div class="input-block">
                    <label for="email-input">Email</label>
                    <input id="email-input" type="email" :disabled="orderData?.hasEmail">
                </div>
                <div class="input-block">
                    <label for="email-input">Email повторно (только ручной ввод):</label>
                    <input id="email-input" type="email" :disabled="orderData?.hasEmail">
                </div>
            </div>
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
    </div>
</template>
<script setup lang="ts">
import codeIcon from "../../assets/icons/lock.svg"
import router from '@/routers/router';
import { paymentService } from '@/services/paymentService';
import { OrderInfoResponse } from '@/types/payment';
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import HeaderPaymentView from '../molecule/HeaderPaymentView.vue';
import notification from '@/plugins/notifications';
import { AxiosError } from 'axios';
import { useRequestHandler } from "@/composable/useRequestHandler";
import { useModalStore } from "@/stores/useModalStore";
import { ModalPropsView } from "@/types/component";
import ModalFormView from "../atom/ModalFormView.vue";

const route = useRoute();

const codeValue = ref<string>("");
const externalId = ref<string>("");
const modalStore = useModalStore();

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

const bookingId = route.query.bookingId;
const orderData = ref<OrderInfoResponse>();
const paymentMethod = ref<string>("SIMULATION");

onMounted(async () => {
    if (bookingId === null) return;
    const response = await paymentService.getOrderInfo(Number(bookingId));
    orderData.value = response.data;
})

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
@use "../../assets/styles/static/mixin" as mixins;
@use "../../assets/styles/static/color";

.payment-wrapper {
    @include mixins.display-center();
    width: 100%;
    overflow-y: hidden;
    flex-direction: column;
    .code-confirmation{
        @include mixins.display-modal();
    }
    .payment-container {
        width: 80%;
        background: #ccc;
        max-width: 1024px;
        padding: 1rem 2rem;
        margin-top: 2rem;

        .header-container {
            @include mixins.display-column();
            gap: 1rem;

            .info-container {
                display: flex;
                align-items: center;
                justify-content: space-between;

                .title {
                    font-size: 1.5rem;
                }

                .price {
                    font-size: 1.25rem;
                }
            }

            .line-container {
                height: 1px;
                background: #000;
            }
        }

        .payment-method-container {
            .label {
                text-transform: uppercase;
                font-size: 1.15rem;
            }
        }

        .email-container {
            @include mixins.display-column();

            .input-block {
                @include mixins.display-column();
            }
        }

        .code-confirmation-container {}
    }
}
</style>