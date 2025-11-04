<template>
  <div class="my-payments-container">
    <table border="1">
      <tr>
        <th>
          ID
        </th>
        <th>
          Описание
        </th>
        <th>
          Способ оплаты
        </th>
        <th>
          Дата
        </th>
        <th>
          Статус
        </th>
        <th>
          Сумма
        </th>
      </tr>
      <tr v-for="payment in paymentsData?.content">
        <td>
          {{ payment.id }}
        </td>
        <td>
          {{ payment.description }}
        </td>
        <td>
          {{ payment.method }}
        </td>
        <td>
          {{ payment.createdAt }}
        </td>
        <td>
          {{ payment.status }}
        </td>
        <td>
          {{ payment.amount }}
        </td>
      </tr>
    </table>
  </div>
</template>

<script setup lang='ts'>
import { paymentService } from '@/shared/services/paymentService';
import { PaginatedPayment } from '@/shared/types/payment';
import { onMounted, ref } from 'vue';

const paymentsData = ref<PaginatedPayment | null>();
onMounted(async() => {
  const response = await paymentService.getMyPayments();
  paymentsData.value = response.data;
})
</script>

<style scoped lang='scss'>
.my-payments-container{
  table{
    
  }
}
</style>