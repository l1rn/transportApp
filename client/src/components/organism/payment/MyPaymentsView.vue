<template>
  <div>
    
  </div>
</template>

<script setup lang='ts'>
import { paymentService } from '@/shared/services/paymentService';
import { onMounted } from 'vue';

onMounted(async() => {
  console.log((await paymentService.getMyPayments()).data);
})
</script>

<style scoped lang='scss'>

</style>