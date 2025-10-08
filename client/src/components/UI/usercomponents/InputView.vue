<template>
    <div class="smart-input-wrapper">
        <div>
            <input 
                v-model="localValue"
            >
        </div>
        <div class="suggestions-list">
            
        </div>
    </div>
</template>
<script setup lang="ts">
import { defineProps, ref } from 'vue';

const props = defineProps<{
    type: string;
    suggestionType: 'from' | 'to' | 'transport';
}>();

const localValue = ref<string | null>(null);

console.log(props);
</script>
<style lang="sass">
    
</style>