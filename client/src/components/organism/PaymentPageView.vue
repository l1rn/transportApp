<template>
    <div class="payment-wrapper">
        <div class="payment-container">
            <div class="header-container">
                <div class="title">
                    title
                </div>
                <div class="price">
                    222 Р
                </div>
            </div>
            <div class="payment-method-container">
                <div class="label">
                    выбор способа оплаты:
                </div>
                <input type="text">
            </div>
            <template>
                <div class="email-container">
                    <div class="input-block">
                        <label for="email-input">Email</label>
                        <input 
                        id="email-input" 
                        type="email">
                    </div>
                    <div class="input-block">
                        <label for="email-input">Email повторно (только ручной ввод):</label>
                        <input 
                        id="email-input" 
                        type="email">
                    </div>
                </div>
                <template>
                    <div class="code-confirmation-container">
                        <input type="text">
                        <button>Подтвердить</button>
                    </div>
                </template>
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
<script setup>
import router from '@/routers/router';

</script>
<style scoped lang="scss">
    
</style>