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
import AddRouteView from '@/components/atom/admin/AddRouteView.vue';
import DeleteRouteView from '@/components/atom/admin/DeleteRouteView.vue';
import PutRouteView from '@/components/atom/admin/PutRouteView.vue';

const tabs = [
    { type: 'add', title: 'Добавление маршрутов', component: AddRouteView },
    { type: 'edit', title: 'Обновление маршрутов', component: PutRouteView },
    { type: 'delete', title: 'Удаление маршрутов', component: DeleteRouteView }
];

const activeTab = ref('add');

const activeComponent = computed(() => {
    return tabs.find(t => t.type === activeTab.value)?.component;
  }
);
</script>
<style scoped lang="sass">
*
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
.admin-panel
  max-width: 1500px
  margin: 0 auto
  padding: 2rem

.buttons-container
  display: flex
  gap: 1rem
  margin-bottom: 2rem
  justify-content: center

  .link
    cursor: pointer
    padding: 1rem 2rem
    border: none
    border-radius: 8px
    background: #f5f5f5
    transition: all 0.3s ease
    font-weight: 500
    color: #333

    &:hover
      background: #e0e0e0
      transform: translateY(-2px)
      box-shadow: 0 2px 8px rgba(0,0,0,0.1)

    &.active
      background: #2196F3
      color: white
      box-shadow: 0 4px 12px rgba(33,150,243,0.3)

.content
  background: white
  border-radius: 12px
  padding: 2rem
  box-shadow: 0 4px 12px rgba(0,0,0,0.1)
  animation: fadeIn 0.3s ease

@keyframes fadeIn
  from
    opacity: 0
    transform: translateY(10px)
  to
    opacity: 1
    transform: translateY(0)
</style>