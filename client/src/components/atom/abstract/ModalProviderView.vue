<template>
  <div class="modal-provider">
    <slot />
    
    <teleport to="body">
      <div class="modal-container">
        <component 
            v-for="modal in modals.values()"
            :key="modal.id"
            :is="modal.component"
            v-bind="modal.props"
            v-model:isOpen="modal.isOpen"
            @close="closeModal(modal.id)"
        />
      </div>
    </teleport>
  </div>
</template>

<script setup lang='ts'>
import { ModalKey, useModal } from '@/composable/useModal';
import { provide } from 'vue';

const modal = useModal();
provide(ModalKey, modal);

const { modals, closeModal } = modal;
</script>

<style scoped lang='scss'>

</style>