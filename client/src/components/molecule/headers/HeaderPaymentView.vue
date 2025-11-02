<template>
    <div class="payment-header-container">
        <div class="text">
            ololoseller
        </div>
        <div class="nav-bar-container">
            <div 
            @click="setView('current-payment')"
            class="current-payment-container"
            :class="{ 'active': currentView === 'current-payment' }">
                <a>
                    Текущий платеж
                </a>
            </div>
            <div 
            @click="setView('my-payments')"
            class="my-payments-container"
            :class="{ 'active': currentView === 'my-payments' }">
                <a>
                    Мои покупки
                </a>
            </div>
            <div 
            @click="setView('promocodes')" 
            class="promocodes-container"
            :class="{ 'active': currentView === 'promocodes' }">
                <a>
                    Мои промокоды
                </a>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { usePaymentNavigation } from '@/composable/usePaymentNavigation';

const { currentView, setView } = usePaymentNavigation();
</script>
<style lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

.payment-header-container{
    @include mixins.display-center();
    background: colors.$green;
    width: 100%;
    padding: 2.5rem 0;
    justify-content: space-evenly;

    .text{
        font-weight: 700;
        font-size: 1.2rem;
        color: colors.$white;
        position: relative;
        &::after{
            content: "💸";
            top: -0.4rem;
            left: -1rem;
            position: absolute;
        }
    }
    .nav-bar-container{
        display: flex;
        gap: 2.5rem;
        color: colors.$background;
        font-weight: 700;
        a {
            cursor: pointer;
            
        }
        .active {
            text-decoration: underline;
        }
    }
}
</style>