<template>
    <div class="payment-wrapper">
        <HeaderPaymentView />
        <div class="page-content">
            <PaymentCardView 
                :bookind-id="Number(bookingId)"
                :orderData="orderData"
            />
        </div>
    </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import HeaderPaymentView from '../molecule/HeaderPaymentView.vue';
import PaymentCardView from '../organism/payment/PaymentCardView.vue';
import { useRoute, useRouter } from 'vue-router';
import { paymentService } from '@/shared/services/paymentService';
import { OrderInfoResponse } from '@/shared/types/payment';

const route = useRoute();
const router = useRouter();
const bookingId = route.query.bookindId;

const orderData = ref<OrderInfoResponse>();

onMounted(async() => {
    if (bookingId === null) return;
    const response = await paymentService.getOrderInfo(Number(bookingId));
    orderData.value = response.data;

    const routeTitle = router.currentRoute.value.meta.title as string;
    if(routeTitle){
        document.title = routeTitle;
    }
})
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