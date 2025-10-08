<template>
    <div class="smart-input-wrapper">
        <div>
            <input 
                v-model="localValue"
                :type="props.type"
            >
        </div>
        <div class="suggestions-list">

        </div>
    </div>
</template>
<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps<{
    type: string;
    suggestionType?: 'from' | 'to' | 'transport';
}>();

const localValue = defineModel<string | null>({
    default: ''
})


</script>
<style lang="sass">
    
</style>