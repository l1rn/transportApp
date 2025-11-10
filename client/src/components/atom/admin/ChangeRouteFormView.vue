<template>
    <div class="change-route-wrapper">
        <div class="change-route-container">
            <div 
            ref="changeFormRef"
            class="change-route-form">
                <div class="title-container">
                    Изменение маршрута
                    <button @click="handleClose">close</button>
                </div>
                <div class="inpul-block">
                    <div class="input-container">
                        <label for="routeFrom">ID</label>
                        <input v-model="updatedRoute.id" type="text" placeholder="1" disabled>
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Откуда</label>
                        <input v-model="updatedRoute.routeFrom" type="text" placeholder="Москва">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Куда</label>
                        <input v-model="updatedRoute.routeTo" type="text" placeholder="Санкт-Петербург">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Время отправления</label>
                        <input type="datetime" v-model="updatedRoute.destinationTime" placeholder="14:20">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Время прибытия</label>
                        <input type="datetime" placeholder="14:24" v-model="updatedRoute.arrivalTime">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Транспорт</label>
                        <input type="text" v-model="updatedRoute.transport" placeholder="Автобус">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Сколько мест</label>
                        <input v-model="updatedRoute.availableSeats" type="text" placeholder="123">
                    </div>
                    <div class="input-container">
                        <label for="routeFrom">Цена</label>
                        <input v-model="updatedRoute.price" type="text" placeholder="5000.00">
                    </div>
                </div>
                <div class="button-container">
                    <button @click="handleSubmit">
                        Применить
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup lang='ts'>
import { Route } from '@/shared/types/route';
import { ref } from 'vue';

const emit = defineEmits(['close', 'submit']);
const changeFormRef = <HTMLElement | null>(null);

const props = defineProps<{
    routeData: Route
}>();

const updatedRoute = ref({ ...props.routeData });
const handleSubmit = () => {
    emit('submit', updatedRoute.value);
}

const handleClose = () => {
    emit('close');
}
</script>
<style scoped lang='scss'>
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.change-route-wrapper {
    @include mixins.display-modal();
    left: 0;
    .change-route-container {
        position: relative;
        @include mixins.display-center();
        height: 100%;
    }
    .change-route-form {
        @include mixins.display-column();
        position: relative;
        background: #fff;
        padding: 1.5rem;
        border-radius: 16px;
        gap: 1rem;
        .input-container{
            @include mixins.display-column();
            input{
                @include mixins.custom-input();
            }
        }
        .button-container{
            button{
                @include mixins.button-clear(colors.$accent, white); 
                width: 100%;
                padding: 0.5rem;
                font-size: 1.05rem;
                border-radius: 8px;
                transition: all 0.3s ease;
                &:hover{
                    background: colors.$primary-blue;
                }
            }
        }
    }
}
</style>