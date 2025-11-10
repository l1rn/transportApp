<template>
    <div class="smart-input-wrapper">
        <div
        ref="containerRef"
        class="input-container">
            <input 
                ref="inputRef"
                v-model="localValue" 
                :type="props.type" 
                :placeholder="props.placeholder"
                :readonly="props.type === 'select'"
                @focus="handleInputFocus"
                @blur="handleInputBlur"
                @keydown="handleKeydown"
            />
            <span 
                class="select-icon" 
                :class="{ 'isActive': isDropdownOpen }"
                @mousedown="handleInputFocus"
                v-if="props.type === 'select'">
                <img src="../../assets/icons/dropdown.svg" alt="select">
            </span>

            <template v-if="props.type !== 'date'">
                <div 
                v-if="isDropdownOpen" 
                class="suggestions-list"
                >
                    <ul>
                        <template 
                        v-if="suggestionType"
                        v-for="(el, idx) in apiResults" 
                        :key="idx"
                        >
                            <li 
                            @mousedown="selectSuggestion(el)">
                                {{ el }}
                            </li>
                        </template>
                        <template 
                        v-if="props.arrayType"
                        v-for="method in dropdownList"
                        :key="method">
                            <li
                            @mousedown="selectSuggestion(method)">{{ method }}</li>
                        </template>
                    </ul>
                </div>
            </template>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useTypeDetection } from '@/composable/useTypeDetection';
import { routesService } from '@/shared/services/routesService';
import { defineProps, onBeforeUnmount, onMounted, ref, watch } from 'vue';

const { onTypeStart, onTypeEnd, cancelTypeDetection } = useTypeDetection();

const isDropdownOpen = ref(false);
const isLoading = ref(false);
const isProcessingSelection = ref(false);

const containerRef = ref<HTMLElement | null>(null);
const inputRef = ref<HTMLInputElement | null>(null);

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

const handleClickOutside = (event: Event) => {
    if(containerRef.value && !containerRef.value.contains(event.target as Node)){
        handleInputBlur();
    }
}

const handleKeydown = (event: KeyboardEvent) => {
    if (props.type === 'select') {
        if (event.key === 'Escape') {
            handleInputBlur();
        } else if (event.key === 'Enter' && !isDropdownOpen.value) {
            handleInputFocus();
        }
    }
};

const dropdownList = ref<Array<string>>();

const transportList = ref<Array<string>>([
  "🚌 Автобус",
  "✈️ Авиа",
  "🚆 Поезд",
  "🏍️ Любой"
])

onMounted(() => {
    document.addEventListener('mousedown', handleClickOutside);
    if(props.arrayType === 'transport'){
        dropdownList.value = transportList.value;
    }
    else{
        dropdownList.value = props.suggestionList;
    }
})

onBeforeUnmount(() => {
    document.removeEventListener('mousedown', handleClickOutside);
})

const handleInputFocus = () => {
    isDropdownOpen.value = true;    
}

const handleInputBlur = () => {
    setTimeout(() => {
        if(!isProcessingSelection.value){
            isDropdownOpen.value = false
        }
    }, 100);
};

const apiResults = ref<string[]>([]);

const fetchSuggestions = async (q: string | null) => {
    if (!q) {
        apiResults.value = [];
        return;
    }

    isLoading.value = true;
    try {
        let response = null;
        if(props.suggestionType === 'from'){
            response = await routesService.findCitiesFrom(q);
        }
        else if(props.suggestionType === 'to'){
            response = await routesService.findCitiesTo(q);
        }
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
        handleInputBlur();
        setTimeout(() => {
            isProcessingSelection.value = false;
        }, 50);
    }, 10);
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

watch(isDropdownOpen, (newValue) => {
    if(newValue && props.suggestionType && !localValue.value){
        fetchSuggestions('');
    }
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
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            transition: all 0.3s;
            z-index: 2;
            &.isActive {
                transform: translateY(-50%) rotate(180deg);
            }
            img {
                width: 100%;
                height: 100%;
                pointer-events: none;
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
    box-shadow: 0 0 8px rgba($color: #000000, $alpha: 0.15);

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
            border-bottom: 1px solid colors.$light-grey;
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