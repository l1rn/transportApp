<template>
    <div class="email-form">
        <div 
        ref="formRef"
        class="email-form-container">
            <div class="text-container">
                <div class="icon">
                    <img 
                    :src="props.icon"
                    alt="email">
                </div>
                <div class="title">
                    {{ props.title }}
                </div>
                <div class="desc">
                    {{ props.desc }}
                </div>
            </div>
            <div class="input-container">
                <input 
                :type="props.inputType"
                v-model="localValue"
                :placeholder="props.inputPlaceholder"
                >
                <button @click="props.submitFunc">
                    {{ props.buttonName }}
                </button>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { useModalStore } from '@/stores/useModalStore';
import { ref } from 'vue';

const props = defineProps<{
    icon: string;
    title: string;
    desc: string;
    inputPlaceholder: string;
    buttonName: string;
    inputType: string;
    submitFunc: () => void | Promise<void>;
    storeKey: string;
}>();

const localValue = defineModel<string | number | null>({
    default: ''
});

const formRef = ref<HTMLElement | null>(null);
const modalStore = useModalStore();

useConditionalClickOutside(
    formRef,
    () => modalStore.isOpen(props.storeKey),
    () => modalStore.close(props.storeKey)
)
</script>
<style scoped lang="scss">
@import "../../assets/styles/static/color.d.scss";
@import "../../assets/styles/static/mixin.d.scss";

.email-form {
    position: relative;
    @include display-center();
    height: 100%;
    .email-form-container{
        background: $white;
        width: 80%;
        max-width: 700px;
        max-height: 500px;
        height: 100%;
        @include display-column(center, center);
        border-radius: 24px;
        gap: 2rem;
        .text-container{
            @include display-column(center, center);
            text-align: center;
            gap: 0.75rem;
            .icon{
                img{
                    width: 56px;
                }
            }
            .title{
                font-size: 24px;
            }
            .desc{
                width: 80%;
                font-size: 18px;
            }
        }
        .input-container {
            @include display-column();
            gap: 1.25rem;
            input {
                @include custom-input();
            }  
            button {
                @include button-clear($accent, $white);
                padding: 0.75rem 1.25rem;
                border-radius: 8px;
                font-size: 1.15rem;
                transition: all 0.3s ease;
                &:hover {
                    background: $primary-blue;
                }
            }
        }
    }
}
</style>