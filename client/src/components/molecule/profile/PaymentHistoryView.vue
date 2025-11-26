<template>
    <div 
    class="payment-history-wrapper">
        <div class="payment-history-container">
            <div 
            ref="modalRef"
            class="grid-wrapper">
                <div class="title-container">
                    <div class="title">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                            fill="#e3e3e3">
                            <path
                                d="M480-120q-138 0-240.5-91.5T122-440h82q14 104 92.5 172T480-200q117 0 198.5-81.5T760-480q0-117-81.5-198.5T480-760q-69 0-129 32t-101 88h110v80H120v-240h80v94q51-64 124.5-99T480-840q75 0 140.5 28.5t114 77q48.5 48.5 77 114T840-480q0 75-28.5 140.5t-77 114q-48.5 48.5-114 77T480-120Zm112-192L440-464v-216h80v184l128 128-56 56Z" />
                        </svg>
                        История оплаты
                    </div>
                    <div @click="handleClose" class="close">
                        <img src="../../../assets/icons/close.png" alt="close">
                    </div>
                </div>
                <div class="handle-table-container">
                    <div class="grid-table">
                        <div class="grid-header">
                            <div class="grid-cell">ID</div>
                            <div class="grid-cell">Описание</div>
                            <div class="grid-cell">Метод</div>
                            <div class="grid-cell">Создано</div>
                            <div class="grid-cell">Пользователь</div>
                            <div class="grid-cell">Сумма</div>
                        </div>
                        <div class="grid-content">
                            <template v-for="payment in props.paymentHistory.content" :key="payment.id">
                                <div class="grid-row">
                                    <div class="grid-cell">
                                        {{ payment.id }}
                                    </div>
                                    <div class="grid-cell">
                                        {{ payment.description }}
                                    </div>
                                    <div class="grid-cell">
                                        {{ payment.method }}
                                    </div>
                                    <div class="time-cell">
                                        <div class="date">{{ payment.createdAt.split('T')[0] }}</div>
                                        <div class="time">
                                            <svg xmlns="http://www.w3.org/2000/svg" height="24px"
                                                viewBox="0 -960 960 960" width="24px" fill="#475569">
                                                <path
                                                    d="M480-80q-75 0-140.5-28.5t-114-77q-48.5-48.5-77-114T120-440q0-75 28.5-140.5t77-114q48.5-48.5 114-77T480-800q75 0 140.5 28.5t114 77q48.5 48.5 77 114T840-440q0 75-28.5 140.5t-77 114q-48.5 48.5-114 77T480-80Zm0-360Zm112 168 56-56-128-128v-184h-80v216l152 152ZM224-866l56 56-170 170-56-56 170-170Zm512 0 170 170-56 56-170-170 56-56ZM480-160q117 0 198.5-81.5T760-440q0-117-81.5-198.5T480-720q-117 0-198.5 81.5T200-440q0 117 81.5 198.5T480-160Z" />
                                            </svg>
                                            {{ payment.createdAt.split('T')[1].split(":")[0]
                                            }}:{{ payment.createdAt.split('T')[1].split(":")[1] }}
                                        </div>
                                    </div>
                                    <div class="grid-cell">
                                        {{ payment.username }}
                                    </div>
                                    <div class="grid-cell price-cell">
                                        {{ payment.amount }} Р.
                                    </div>
                                </div>
                            </template>
                        </div>
                    </div>
                    <div class="button-container">
                        <button :disabled="props.page <= 0" @click="handleBackward">Назад</button>
                        {{ props.page + 1 }} из {{ props.paymentHistory.totalPages }}
                        <button :disabled="props.page >= props.paymentHistory.totalPages - 1"
                            @click="handleForward">Вперед</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { useConditionalClickOutside } from "@/composable/useConditionalClickOutside";
import { PaymentHistoryResponse } from "@/shared/types/payment";
import { PaginatedResponse } from "@/shared/types/response";
import { ref } from "vue";
const emit = defineEmits(['forward', 'backward', 'close'])

const props = defineProps<{
    paymentHistory: PaginatedResponse<PaymentHistoryResponse>;
    page: number;
    isHistoryOpen: boolean;
}>();

const modalRef = ref<HTMLElement | null>(null);

const handleClose = () => {
    emit('close');
}

const handleForward = () => {
    emit('forward');
}

const handleBackward = () => {
    emit('backward');
}

useConditionalClickOutside(
    modalRef,
    () => props.isHistoryOpen,
    () => handleClose()
)
</script>
<style scoped lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;
$grid-columns: minmax(60px, 0.5fr) minmax(200px, 4fr) minmax(120px, 1fr) minmax(120px, 1fr) minmax(120px, 1fr) minmax(120px, 1fr);

.payment-history-wrapper {
    @include mixins.display-modal();

    .payment-history-container {
        @include mixins.display-center();
        position: relative;
        width: 100%;
        height: 100%;
    }

    .handle-table-container {
        @include mixins.display-column();
        gap: 1rem;

        .button-container {
            @include mixins.display-center();
            gap: 1rem;

            button {
                @include mixins.button-clear(colors.$accent, white);
                padding: 1rem 2rem;
                font-size: 16px;
                font-weight: 600;
                border-radius: 16px;
                transition: all 0.3s ease;

                &:hover {
                    background: colors.$primary-blue;
                }

                &:disabled {
                    background: #6c757d;
                    cursor: not-allowed;
                }
            }
        }
    }

    .grid-wrapper {
        @include mixins.display-column();
        width: 90vw;
        height: 90vh;
        background: white;
        gap: 1rem;
        padding: 1.5rem;
        border-radius: 16px;
        box-sizing: border-box;

        .title-container {
            display: flex;
            justify-content: space-between;
            flex-shrink: 0;

            .title {
                @include mixins.display-center();
                justify-content: start;
                font-size: 24px;
                gap: 0.5rem;

                svg {
                    fill: colors.$medium-grey;
                }
            }

            .close {
                @include mixins.display-center();
                cursor: pointer;
                transition: all 0.3s ease;

                img {
                    width: 16px;
                }

                &:hover {
                    transform: rotate(90deg);
                }
            }
        }

        .grid-table {
            .grid-header {
                background: #2d3748;
                color: white;
                border-radius: 16px 16px 0 0;
            }

            .grid-header,
            .grid-row {
                display: grid;
                grid-template-columns: $grid-columns;
            }

            .grid-row {
                background: white;
                transition: all 0.3s ease;

                &:nth-child(even) {
                    background: #fdfdfe;
                }

                &:last-child {
                    border-radius: 0 0 16px 16px;
                }

                &:hover {
                    background: #e2e8f0;
                }
            }

            .grid-cell {
                @include mixins.display-word-breaking();
                @include mixins.display-center();
                text-align: center;
                padding: 0.5rem;
                min-width: 0;
            }

            .price-cell {
                color: colors.$medium-green;
            }

            .time-cell {
                @include mixins.display-column();
                @include mixins.display-center();
                padding: 0.5rem;
                color: colors.$light-black;

                .time {
                    display: flex;
                    gap: 0.25rem;
                    @include mixins.display-center();
                    background: #f7f3ff;
                    padding: 0.15rem;
                    font-size: 13px;
                    border-radius: 8px;

                    svg {
                        width: 16px;
                        fill: colors.$light-black;
                    }

                }
            }
        }
    }
}
</style>
