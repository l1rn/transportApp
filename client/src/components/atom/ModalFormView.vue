<template>
    <div class="email-form">
        <div ref="formRef" class="email-form-container">
            <div class="header-container">
                <div 
                @click="modalStore.close(props.storeKey)"
                class="close-container">
                    <img src="../../assets/icons/close.png" alt="">
                </div>
            </div>
            <div class="text-container">
                <div class="icon">
                    <img :src="props.icon" alt="email">
                </div>
                <div class="title">
                    {{ props.title }}
                </div>
                <div class="desc">
                    {{ props.desc }}
                </div>
            </div>
            <div class="input-container">
                <input v-model="localValue" :type="props.inputType" :placeholder="props.inputPlaceholder">
                <button @click="props.submitFunc">
                    {{ props.buttonName }}
                </button>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { useModalStore } from '@/shared/stores/useModalStore';
import { ModalPropsView } from '@/shared/types/component';
import { ref } from 'vue';

const props = defineProps<ModalPropsView>();

const localValue = defineModel<string | number | null>({
    default: ''
});

const formRef = ref<HTMLElement | null>(null);
const modalStore = useModalStore();

useConditionalClickOutside(
    formRef,
    () => modalStore.isOpen(props.storeKey!),
    () => modalStore.close(props.storeKey!)
)
</script>
<style scoped lang="scss">
@use "../../assets/styles/static/color" as colors;
@use "../../assets/styles/static/mixin" as mixins;

.email-form {
    position: relative;
    @include mixins.display-center();
    flex-direction: column;
    height: 100%;

    .header-container {
        width: 90%;
        .close-container {
            display: flex;
            align-items: start;
            justify-content: end;
            cursor: pointer;
            img {
                width: 16px;
                height: 16px;
                transition: all 0.3s ease;

                &:hover {
                    transform: rotate(90deg);

                }
            }
        }
    }

    .email-form-container {
        background: colors.$white;
        width: 80%;
        max-width: 700px;
        max-height: 450px;
        height: 100%;
        @include mixins.display-column(center, center);
        border-radius: 24px;
        gap: 2rem;

        .text-container {
            @include mixins.display-column(center, center);
            text-align: center;
            gap: 0.75rem;

            .icon {
                img {
                    width: 56px;
                }
            }

            .title {
                font-size: 24px;
            }

            .desc {
                width: 80%;
                font-size: 18px;
            }
        }

        .input-container {
            @include mixins.display-column();
            gap: 1.25rem;

            input {
                @include mixins.custom-input();
            }

            button {
                @include mixins.button-clear(colors.$accent,
                    colors.$white );
                padding: 0.75rem 1.25rem;
                border-radius: 8px;
                font-size: 1.15rem;
                transition: all 0.3s ease;

                &:hover {
                    background: colors.$primary-blue;
                }
            }
        }
    }
}
</style>