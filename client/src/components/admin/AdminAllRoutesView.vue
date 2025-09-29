<template>
  <div class="admin-panel">
    <div class="buttons-container">
      <button
        v-for="tab in tabs"
        :key="tab.type"
        class="link"
        :class="{ active: activeTab === tab.type }"
        @click="activeTab = tab.type"
      >
        {{ tab.title }}
      </button>
    </div>

    <component
      :is="activeComponent"
      class="content"
    />
  </div>
</template>
<script setup>
import { ref, computed } from 'vue';
import AddRoute from './adminRoutesManage/AddRouteView.vue';
import DeleteRoute from './adminRoutesManage/DeleteRouteView.vue';
import PutRoute from './adminRoutesManage/PutRouteView.vue';

const tabs = [
    { type: 'add', title: 'Добавление маршрутов', component: AddRoute },
    { type: 'edit', title: 'Обновление маршрутов', component: PutRoute },
    { type: 'delete', title: 'Удаление маршрутов', component: DeleteRoute }
];

const activeTab = ref('add');

const activeComponent = computed(() => {
    return tabs.find(t => t.type === activeTab.value)?.component;
  }
);
</script>
<script>
export default {
    name: "AdminAllRoutes"
}
</script>
<style scoped lang="sass">
@import "@/assets/styles/adminObjects/allroutes";
</style>