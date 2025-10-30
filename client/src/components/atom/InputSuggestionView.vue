<template>
    <div class="smart-input-wrapper">
        <div class="input-container">
            <input 
                v-model="localValue" 
                :type="props.type" 
                :placeholder="props.placeholder"
                :readonly="props.type === 'select'"
                @click="focusSuggestions"
                @blur="hideSuggestions"
            />
            <span 
                class="select-icon" 
                :class="{ 'isActive': suggestionList}"
                v-if="props.type === 'select'">
                <img src="../../assets/icons/dropdown.svg" alt="select">
            </span>

            <template v-if="props.type !== 'date'">
                <div 
                v-if="suggestionList" 
                class="suggestions-list">
                    <ul>
                        <template 
                        v-if="suggestionType"
                        v-for="(el, idx) in apiResults" 
                        :key="idx"
                        >
                            <li 
                            @click="selectSuggestion(el)">
                                {{ el }}
                            </li>
                        </template>
                        <template 
                        v-if="props.arrayType"
                        v-for="transport in props.suggestionList"
                        :key="transport">
                            <li
                            @click="selectSuggestion(transport)">{{ transport }}</li>
                        </template>
                    </ul>
                </div>
            </template>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useTypeDetection } from '@/composable/useTypeDetection';
import { routesService } from '@/services/routeService';
import { defineProps, ref, watch } from 'vue';

const { onTypeStart, onTypeEnd, cancelTypeDetection } = useTypeDetection();

const suggestionList = ref(false);
const isLoading = ref(false);
const isProcessingSelection = ref(false);

const props = defineProps<{
    type: string;
    arrayType?: 'transport' | 'self';
    suggestionList?: Array<string>;
    suggestionType?: 'from' | 'to';
    placeholder?: string;
}>();

const localValue = defineModel<string | null>({
    default: ''
});

const focusSuggestions = () => {
    suggestionList.value = true;
}

const hideSuggestions = () => {
    if(props.suggestionType){
        setTimeout(() => {
            suggestionList.value = false;
        }, 250)
    }
    else{
        setTimeout(() => {
            suggestionList.value = false;
        }, 100)
    }
}

const apiResults = ref<string[]>([]);

const fetchSuggestions = async (q: string | null) => {
    if (!q) {
        apiResults.value = [];
        return;
    }

    isLoading.value = true;
    try {
        let response = null;
        response = await routesService.findAllCities(q);
        apiResults.value = response?.data.data || [];
    }
    catch (error: any) {
        console.error(error);
    }
    finally {
        isLoading.value = false;
    }
}

const selectSuggestion = (item: any) => {
    isProcessingSelection.value = true;
    setTimeout(() => {
        localValue.value = item;
        apiResults.value = [];
        hideSuggestions();
        setTimeout(() => {
            isProcessingSelection.value = false;
        }, 100);
    }, 0);
}

watch(localValue, (newValue) => {
    if(isProcessingSelection.value){
        return;
    }

    if(!newValue?.trim()){
        cancelTypeDetection();
        apiResults.value = [];
        isLoading.value = false;
        return;
    }

    onTypeStart();
    onTypeEnd(() => {
        isLoading.value = true;
        fetchSuggestions(newValue);
    })
})
</script>
<style lang="scss">
@use "../../assets/styles/static/color" as colors;
@use "../../assets/styles/static/mixin" as mixins;

.smart-input-wrapper {
    position: relative;

    .input-container {
        position: relative;
        input {
            @include mixins.custom-input();
            width: 100%;
        }
        input[type="select"]{
            cursor: pointer;
            caret-color: transparent;
        }
        .select-icon {
            position: absolute;
            width: 16px;
            height: 16px;
            right: 16px;
            top: 45%;
            transform: translateY(-50%);
            cursor: pointer;
            transition: all 0.3s;
            &.isActive {
                transform: scaleY(-1);
                top: 42.5%;
            }
        }

    }
}

.suggestions-list {
    position: absolute;
    background: colors.$white;
    width: 100%;
    top: 100%;
    left: 0;
    z-index: 10;
    border-radius: 8px;

    ul {
        padding-left: 0;
        margin: 0;
        list-style-type: none;
        max-height: 200px;
        overflow-y: scroll;
        
        scrollbar-width: thin;
        li {
            position: relative;
            cursor: pointer;
            padding: 0.3rem 1rem;
            font-weight: 600;
            border-bottom: 1px solid colors.$light-gray;
            &:hover {
                background: colors.$hover-white;
            }
            &:first-child {
                border-radius: 8px 8px 0 0;
            }
            &:last-child{
                border-radius: 0 0 8px 8px;
            }
        }
    }
}
</style>