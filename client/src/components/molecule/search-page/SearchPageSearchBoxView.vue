<template>
    <div class="search-container">
        <div class="input-block">
            <div class="input-container">
                <label for="from">
                    Откуда
                </label>
                <InputSuggestionView
                    id="from" 
                    v-model="filter.routeFrom"
                    type="text"
                    suggestion-type="from"
                    placeholder="Москва"
                    @update:model-value="handleInputChange"
                />
            </div>
            <div class="input-container">
                <label for="to">
                    Куда
                </label>
                <InputSuggestionView 
                    v-model="filter.routeTo"
                    type="to"
                    suggestion-type="to"
                    placeholder="Самара"
                    @update:model-value="handleInputChange"
                />
            </div>
            <div class="input-container">
                <label for="date">
                    Дата вылета
                </label>
                <InputSuggestionView
                    id="date" 
                    v-model="filter.date"
                    type="date"
                    @update:model-value="handleInputChange"
                    />
            </div>
            <div class="input-container">
                <label for="transport">Желаемый транспорт</label>
                <InputSuggestionView
                    id="transport"
                    v-model="filter.transport" 
                    type="select"
                    :suggestion-list="transportList"
                    array-type="transport"
                    placeholder="Выберите"
                    @update:model-value="handleInputChange" />
            </div>
        </div>
        <div class="button-block">
            <button @click="handleSearch">Применить</button>
            <button @click="handleClear">Сбросить</button>
        </div>
    </div>
</template>
<script setup lang='ts'>
import InputSuggestionView from '@/components/atom/InputSuggestionView.vue';
import { RouteFilter } from '@/shared/types/route';
import { useFormatUtils } from '@/shared/utils/formatUlils';
import { ref } from 'vue';

const emit = defineEmits<{
    search: [filter: RouteFilter],
    resetPage: []
}>();

const filter = ref<RouteFilter>({});
const formatUtils = useFormatUtils();

const transportList = ref<Array<string>>([
  "🚌 Автобус",
  "✈️ Авиа",
  "🚆 Поезд",
  "🏍️ Любой"
])

const handleSearch = () => {
    const newFilter = formatUtils.removeEmojiForTransport(filter.value);
    const cleanFilter = Object.fromEntries(
        Object.entries(newFilter).filter(([_, value]) => 
            value !== undefined && value !== null && value !== ''
        )
    ) as RouteFilter;

    emit('search', cleanFilter);
}

let searchTimeout: ReturnType<typeof setTimeout> | null = null;

const handleInputChange = () => {
    if(searchTimeout){
        clearTimeout(searchTimeout);
    }

    searchTimeout = setTimeout(() => {
        handleSearch();
    }, 750);
}

const handleClear = () => {
    filter.value = {};
    emit('resetPage');
    emit('search', {});
}
</script>
<style scoped lang='scss'>
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.search-container {
    display: flex;
    background: #f0f8f0; 
    border-radius: 16px;
    padding: 1.5rem;
    gap: 1rem;
    box-shadow: 0 0 8px rgba($color: #000000, $alpha: 0.15);
    .input-block {
        display: flex;
        gap: 0.5rem;
        width: 80%;
        .input-container{
            @include mixins.display-column();
            width: 100%;
            gap: 0.25rem;
            label{
                font-size: 1.05rem;
                font-weight: 600;
                color: #263238;
            }
        }
    }
    .button-block{
        display: flex;
        gap: 1rem;
        button {
            font-size: 1.1rem;
            border-radius: 8px;
            padding: 0 2rem;
            transition: all 0.3s ease;
            &:first-child {
                @include mixins.button-clear(#2e8b57, white);
                &:hover {
                    background: colors.$medium-green;
                }
            }
            &:last-child {
                @include mixins.button-clear(transparent, #6c757d);
                box-sizing: content-box;
                border: 2px solid #6c757d;
                &:hover {
                    background: #5a6268;
                    color: white;
                }
            }
        }
    }
}   
</style>