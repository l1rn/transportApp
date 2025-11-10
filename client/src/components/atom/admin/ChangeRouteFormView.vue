<template>
    <div class="change-route-wrapper">
        <div class="change-route-form">
            <div class="title-container">
                Изменение маршрута
            </div>
            <div class="inpul-block">
                <div class="input-container">
                    <label for="routeFrom">ID</label>
                    <input 
                    v-model="updatedRoute?.id"
                    type="text" 
                    placeholder="1"
                    disabled>
                </div>
                <div class="input-container">
                    <label for="routeFrom">Откуда</label>
                    <input 
                    v-model="updatedRoute?.routeFrom"
                    type="text" 
                    placeholder="Москва">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Куда</label>
                    <input 
                    v-model="updatedRoute?.routeTo"
                    type="text" 
                    placeholder="Санкт-Петербург">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Время отправления</label>
                    <input 
                    type="datetime" 
                    v-model="updatedRoute?.destinationTime"
                    placeholder="14:20">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Время прибытия</label>
                    <input 
                    type="datetime" 
                    placeholder="14:24"
                    v-model="updatedRoute?.availableSeats">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Транспорт</label>
                    <input 
                    type="text" 
                    v-model="updatedRoute?.transport"
                    placeholder="Автобус">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Сколько мест</label>
                    <input 
                    v-model="updatedRoute?.availableSeats"
                    type="text" 
                    placeholder="123">
                </div>
                <div class="input-container">
                    <label for="routeFrom">Цена</label>
                    <input 
                    v-model="updatedRoute?.price"
                    type="text" 
                    placeholder="5000.00">
                </div>
            </div>
            <div class="button-container">
                <button @click="handleSubmit">
                    Применить
                </button>
            </div>
        </div>
    </div>
</template>
<script setup lang='ts'>
import { Route } from '@/shared/types/route';
import { onMounted, ref } from 'vue';

const props = defineProps<Route>();
const updatedRoute = ref<Route>();

onMounted(() => {
    updatedRoute.value = props;
})

const handleSubmit = () => {

}
</script>
<style scoped lang='scss'></style>