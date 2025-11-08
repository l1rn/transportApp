<template>
  <div class="my-payments-container">
    <div class="grid-table">
      <div class="grid-header">
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/id.svg" alt="">
          ID 
        </div>
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/desc.svg" alt="">
          Описание 
        </div>
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/method.svg" alt="">
          Способ оплаты
        </div>
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/date.svg" alt="">
          Дата
        </div>
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/status.svg" alt="">
          Статус
        </div>
        <div class="grid-cell">
          <img src="../../../assets/icons/payment/amount.svg" alt="">
          Сумма
        </div>
      </div>
      <div class="grid-content">
        <template
          v-for="payment in paymentsData?.content"
          :key="payment.id">
          <div class="grid-row">
            <div class="grid-cell">
            {{ payment.id }}
            </div>
            <div class="grid-cell">
            {{ payment.description }}
            </div>
            <div class="grid-cell">
            {{ payment.method }}
            </div>
            <div class="grid-cell">
            {{ formatUtils.formatISOString(payment.createdAt) }}
            </div>
            <div class="grid-cell">
            {{ payment.status }}
              <span class="img-status">
                <img 
                v-if="payment.status === 'SUCCEEDED'"
                src="../../../assets/icons/payment/statuses/succeeded.svg" alt="">
                <img 
                v-else-if="payment.status === 'FAILED'"
                src="../../../assets/icons/payment/statuses/failed.svg" alt="">
                <img 
                v-else-if="payment.status === 'CANCELED'"
                src="../../../assets/icons/payment/statuses/canceled.svg" alt="">
                <img 
                v-else-if="payment.status === 'PENDING'"
                src="../../../assets/icons/payment/statuses/pending.svg" alt="">
              </span>
            </div>
            <div class="grid-cell">
            {{ payment.amount }} Р.
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang='ts'>
import { paymentService } from '@/shared/services/paymentService';
import { PaginatedPayment } from '@/shared/types/payment';
import { useFormatUtils } from '@/shared/utils/formatUlils';
import { onMounted, ref } from 'vue';

const paymentsData = ref<PaginatedPayment | null>();
onMounted(async() => {
  const response = await paymentService.getMyPayments();
  paymentsData.value = response.data;
  
})

const formatUtils = useFormatUtils();
</script>

<style scoped lang='scss'>
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

$grid-gap: 1fr 10fr 2.5fr 1.5fr 3fr 1.5fr;

img{
  margin: 0;
  padding: 0;
}
.my-payments-container{
  width: 80%;
  .grid-table{
    @include mixins.display-column();
    margin-top: 1rem;
    box-shadow: 0 0 8px rgba($color: #000000, $alpha: 0.15);
    border-radius: 16px;
    .grid-header, .grid-row{
      display: grid;
      grid-template-columns: $grid-gap;
      box-sizing: border-box;
      transition: all 0.3s ease;
    }

    .grid-header{
      .grid-cell{
        @include mixins.display-center();
        padding: 0.5rem 0;
      }
    }
    
    .grid-row{
      &:hover{
        background: #eefafd;
        transform: translateY(2px) scale(1.01);
      }
      &:last-child{
        .grid-cell{
          &:first-child{
            border-radius: 0 0 0 16px;
          }
          &:last-child{
            border-radius: 0 0 16px 0;
          }
        }
        border-radius: 0 0 16px 16px;
      }

    }

    .grid-cell{
      @include mixins.display-center();
      gap: 0.25rem;
      padding: 0.25rem;
      text-align: center;
      
      &:not(:last-child){
        border-bottom: 2px solid colors.$light-grey; 
        border-right: 2px solid colors.$light-grey;   
      }
      &:last-child{
        border-bottom: 2px solid colors.$light-grey; 
      }
      .img-status{
        @include mixins.display-center();
      }
    }
  }
}
</style>