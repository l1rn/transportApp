<template>
    <div class="payment-header-container">
        <div class="text">
            ololoseller
        </div>
        <div class="nav-bar-container">
            <div class="my-payments-container">
                <a>
                    Мои покупки
                </a>
            </div>
            <div class="promocodes-container">
                <a>
                    Мои промокоды
                </a>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">


</script>
<style lang="scss">
@use "../../assets/styles/static/color" as colors;
@use "../../assets/styles/static/mixin" as mixins;

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
    }
}
</style>