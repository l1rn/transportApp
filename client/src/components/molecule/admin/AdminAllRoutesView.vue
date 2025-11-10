<template>
  <div class="admin-panel">
    <template v-if="props.mode === 'add'">
      <AddRouteView />
    </template>
    <template v-else-if="props.mode === 'edit'">
      <PutRouteView />
    </template>
    <template v-else-if="props.mode === 'delete'">
      <DeleteRouteView />
    </template>
  </div>
</template>
<script setup lang="ts">
import AddRouteView from '@/components/atom/admin/AddRouteView.vue';
import DeleteRouteView from '@/components/atom/admin/DeleteRouteView.vue';
import PutRouteView from '@/components/atom/admin/PutRouteView.vue';
import { ActionType } from '@/shared/types/component';

interface Props {
  mode: ActionType;
}

const props = defineProps<Props>();


</script>
<style scoped lang="scss">
.admin-panel{
  margin-left: 1.5rem;
}
</style>