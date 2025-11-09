<template>
  <div class="payment-method-container">
    <div class="label">
        выбор способа оплаты:
    </div>
    <div class="input">
        <InputSuggestionView
            v-model="paymentMethod"
            type="select"
            placeholder="Способ оплаты"
            array-type="self"
            :suggestion-list="paymentMethods"
        />
    </div>
</div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">

</style>