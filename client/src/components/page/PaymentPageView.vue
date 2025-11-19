<template>
    <div class="payment-wrapper">
        <HeaderPaymentView />
        <div class="page-content">
            <PaymentCardView 
                v-if="currentView === 'current-payment'" 
            />
            <MyPaymentsView v-else-if="currentView === 'my-payments'" />
            <PromocodesView v-else-if="currentView === 'promocodes'" />
        </div>
    </div>
</template>
<script setup lang="ts">
import HeaderPaymentView from '../molecule/headers/HeaderPaymentView.vue';
import PaymentCardView from '../organism/payment/PaymentCardView.vue';
import MyPaymentsView from '../organism/payment/MyPaymentsView.vue';
import { usePaymentNavigationProvider } from '@/composable/usePaymentNavigation';
import PromocodesView from '../organism/payment/PromocodesView.vue';

const { currentView } = usePaymentNavigationProvider();
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
        overflow-y: hidden;
    }
}
</style>