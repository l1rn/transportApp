<template>
    <div class="payment-wrapper">
        <div class="payment-container">
            <div class="header-container">
                <div class="info-container">
                    <div class="title">
                    Товар: {{ orderData?.orderFullName }}
                    </div>
                    <div class="price">
                        {{ orderData?.price}} Р
                    </div>
                </div>
                <div class="line-container"></div>
                <div class="payment-method-container">
                    <div class="label">
                        выбор способа оплаты:
                    </div>
                    <input type="text">
                    <template 
                    v-if="orderData?.paymentMethods">
                        <div 
                        v-for="method in orderData.paymentMethods"
                        :key="method">
                            {{ method }}
                        </div>       
                    </template>
                </div>
            </div>
            
            <div class="email-container">
                <div class="input-block">
                    <label for="email-input">Email</label>
                    <input 
                    id="email-input" 
                    type="email"
                    :disabled="orderData?.hasEmail">
                </div>
                <div class="input-block">
                    <label for="email-input">Email повторно (только ручной ввод):</label>
                    <input 
                    id="email-input" 
                    type="email"
                    :disabled="orderData?.hasEmail">
                </div>
            </div>
            <template>
                <div class="code-confirmation-container">
                    <input type="text">
                    <button>Подтвердить</button>
                </div>
            </template>
            <div class="button-container">
                <button>Оплатить</button>
                <a @click="router.replace('/home')">
                    Отказаться от оплаты и вернуться на главную
                </a>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import router from '@/routers/router';
import { paymentService } from '@/services/paymentService';
import { OrderInfoResponse } from '@/types/payment';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const bookingId = route.query.bookingId;
const orderData = ref<OrderInfoResponse>();

onMounted(async() => {
    if(bookingId === null) return;
    const response = await paymentService.getOrderInfo(Number(bookingId));
    orderData.value = response.data;
})
</script>
<style scoped lang="scss">
@import "../../assets/styles/static/mixin.d.scss";
@import "../../assets/styles/static/color.d.scss";

.payment-wrapper{
    @include display-center();
    width: 100%;
    overflow-y: hidden;

    .payment-container{
        width: 80%;
        background: #ccc;
        max-width: 1024px;
        padding: 1rem 2rem;
        margin-top: 2rem;

        .header-container{
            @include display-column();
            gap: 1rem;
            .info-container{
                display: flex;
                align-items: center;
                justify-content: space-between;
                .title{
                    font-size: 1.5rem;
                }
                .price {
                    font-size: 1.25rem;
                }
            }
            .line-container{
                height: 1px;
                background: #000;
            }
        }
        
        .payment-method-container{
            .label{
                text-transform: uppercase;
                font-size: 1.15rem;
            }
        }   
        .email-container{
            @include display-column();
            .input-block{
                @include display-column();
            }
        }
        .code-confirmation-container{

        }
    }
}
</style>