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
                :readonly="props.type === 'select' && !isSelectActive"
                @click="handleInputClick"
                @focus="handleInputFocus"
                @blur="handleInputBlur"
                @keydown="handleKeydown"
            />
            <span 
                class="select-icon" 
                :class="{ 'isActive': isDropdownOpen }"
                v-if="props.type === 'select'"
                @mousedown="handleIconMouseDown"
                @click="handleInputClick">
                <img src="../../assets/icons/dropdown.svg" alt="select">
            </span>

            <template v-if="props.type !== 'date'">
                <div 
                v-if="isDropdownOpen" 
                class="suggestions-list"
                @mousedown="handleSuggestionsMouseDown"
                >
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
                        v-for="method in props.suggestionList"
                        :key="method">
                            <li
                            @click="selectSuggestion(method)">{{ method }}</li>
                        </template>
                    </ul>
                </div>
            </template>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useTypeDetection } from '@/composable/useTypeDetection';
import { routesService } from '@/shared/services/routeService';
import { defineProps, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue';

const { onTypeStart, onTypeEnd, cancelTypeDetection } = useTypeDetection();

const isDropdownOpen = ref(false);
const isSelectActive = ref(false);
const isLoading = ref(false);
const isProcessingSelection = ref(false);
const ignoreNextBlur = ref(false);
const isIconClick = ref(false);

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
        closeDropdown();
    }
}

const handleKeydown = (event: KeyboardEvent) => {
    if (props.type === 'select') {
        if (event.key === 'Escape') {
            closeDropdown();
        } else if (event.key === 'Enter' && !isDropdownOpen.value) {
            openDropdown();
        }
    }
};

onMounted(() => {
    document.addEventListener('mousedown', handleClickOutside);
})

onBeforeUnmount(() => {
    document.removeEventListener('mousedown', handleClickOutside);
})

const handleInputClick = (event: MouseEvent) => {
    if(props.type === 'select'){
        event.preventDefault();
        event.stopPropagation();
        
        if(!isDropdownOpen.value){
            openDropdown();
        }
    }
}

const handleInputFocus = () => {
    if(props.type === 'select' && !isIconClick.value){
        openDropdown();
    }
    isIconClick.value = false;
}

const handleInputBlur = () => {
    if (!ignoreNextBlur.value && !isIconClick.value) {
        setTimeout(() => {
            if (!isProcessingSelection.value) {
                closeDropdown();
            }
        }, 150);
    }
    ignoreNextBlur.value = false;
};

const handleSuggestionsMouseDown = () => {
    ignoreNextBlur.value = true;
}

const handleIconMouseDown = (event: MouseEvent) => {
    event.preventDefault();
    ignoreNextBlur.value = true;
}

const openDropdown = () => {
    if (props.type === 'select') {
        isDropdownOpen.value = true;
        isSelectActive.value = true;
        
        nextTick(() => {
            inputRef.value?.focus();
            
            if (props.suggestionType && !localValue.value) {
                fetchSuggestions('');
            }
        });
    }
}

const closeDropdown = () => {
    isDropdownOpen.value = false;
    isSelectActive.value = false;
};

const toggleDropdown = () => {
    if(isDropdownOpen.value){
        closeDropdown();
    }
    else{
        openDropdown();
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
        closeDropdown();
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