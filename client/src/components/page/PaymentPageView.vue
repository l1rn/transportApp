<template>
    <div class="payment-wrapper">
        <HeaderPaymentView />
        <div class="page-content">
            <PaymentCardView />

        </div>
    </div>
</template>
<script setup lang="ts">
import HeaderPaymentView from '../molecule/HeaderPaymentView.vue';
import PaymentCardView from '../organism/payment/PaymentCardView.vue';


</script>
<style scoped lang="scss">
@use "../../assets/styles/static/mixin" as mixins;
@use "../../assets/styles/static/color" as colors;

* {
    color: colors.$dark-grey;
}

.payment-wrapper {
    
    .code-confirmation{
        @include mixins.display-modal();
    }
    .page-content{
        @include mixins.display-center();
        width: 100%;
    
    }
}
</style>