<template>
    <div class="change-route-wrapper">
        <div class="change-route-container">
            <div ref="changeFormRef" class="change-route-form">
                <div class="title-container">
                    <div class="text-content">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                            fill="#e3e3e3">
                            <path
                                d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z" />
                        </svg> 
                        Изменение маршрута
                    </div>
                    <button @click="handleClose">
                        <img src="../../../assets/icons/close.png" alt="close">
                    </button>
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
                        <InputSuggestionView type="select" array-type="transport" v-model="updatedRoute.transport"
                            placeholder="Автобус" />
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
import { onBeforeUnmount, onMounted, ref } from 'vue';
import InputSuggestionView from '../InputSuggestionView.vue';
import { useFormatUtils } from '@/shared/utils/formatUlils';

const emit = defineEmits(['close', 'submit']);
const changeFormRef = ref<HTMLElement | null>(null);

const props = defineProps<{
    routeData: Route
}>();

const updatedRoute = ref({ ...props.routeData });
const handleClickOutside = (event: Event) => {
    if (changeFormRef.value && !changeFormRef.value.contains(event.target as HTMLElement)) {
        handleClose();
    }
}

const handleSubmit = () => {
    updatedRoute.value.transport = useFormatUtils().removeEmojiFromRouteData(updatedRoute.value.transport);
    emit('submit', updatedRoute.value);
}

const handleClose = () => {
    emit('close');
}

onMounted(() => {
    document.addEventListener('click', handleClickOutside);
})

onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside);
})
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

    .inpul-block{
        @include mixins.display-column();
        gap: 0.5rem;
    }

    .change-route-form {
        @include mixins.display-column();
        min-width: 800px;
        position: relative;
        background: #fff;
        padding: 1.5rem;
        border-radius: 16px;
        gap: 1rem;

        .input-container {
            @include mixins.display-column();
            gap: 0.25rem;
            label{
                font-size: 17px;
                font-weight: 500;
            }
            input {
                @include mixins.custom-input();
            }
        }

        .button-container {
            button {
                @include mixins.button-clear(colors.$accent, white);
                width: 100%;
                padding: 0.5rem;
                font-size: 1.05rem;
                border-radius: 8px;
                transition: all 0.3s ease;

                &:hover {
                    background: colors.$primary-blue;
                }
            }
        }

        .title-container {
            @include mixins.display-center();
            justify-content: space-between;
            font-size: 24px;
            font-weight: 600;

            .text-content {
                @include mixins.display-center();
                gap: 0.5rem;
                svg{
                    fill: #929191;
                }
            }

            button {
                @include mixins.button-clear(transparent);

                img {
                    width: 16px;
                    transition: all 0.3s ease;

                    &:hover {
                        transform: rotate(90deg);
                    }
                }
            }
        }
    }
}
</style>