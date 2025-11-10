<template>
  <div class="admin-panel">
    <div class="mode-container">
      <span
        @click="selectType('add')"
        class="add-container"
        :class="{ isActive: typeSelected === 'add' }"
      >
        <svg
          :class="{ isActive: typeSelected === 'add' }"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#e3e3e3"
        >
          <path d="M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z" />
        </svg>
        Добавить
      </span>
      <span
        @click="selectType('edit')"
        class="edit-container"
        :class="{ isActive: typeSelected === 'edit' }"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#e3e3e3"
        >
          <path
            d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"
          />
        </svg>
        Изменить
      </span>
    </div>
    <div class="content-container">
      <template v-if="typeSelected === 'add'">
        <AddRouteView />
      </template>
      <template v-else-if="typeSelected === 'edit'">
        <EditRouteView />
      </template>
    </div>
  </div>
</template>
<script setup lang="ts">
import AddRouteView from "@/components/molecule/admin/AddRouteView.vue";
import EditRouteView from "@/components/molecule/admin/EditRouteView.vue";
import { ActionType } from "@/shared/types/component";
import { ref } from "vue";

const typeSelected = ref<ActionType>("add");

const selectType = (type: ActionType) => {
  switch (type) {
    case "add":
      typeSelected.value = "add";
      break;
    case "edit":
      typeSelected.value = "edit";
      break;
    case "delete":
      typeSelected.value = "delete";
      break;
    default:
      typeSelected.value = "add";
  }
};
</script>
<style scoped lang="scss">

@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;
.admin-panel {
  @include mixins.display-column();
  gap: 1rem;
}

.mode-container {
  display: flex;
  gap: 0.75rem;
  span {
    @include mixins.display-center();
    gap: 0.25rem;
    font-weight: 600;
    cursor: pointer;
    background: white;
    border: 2px solid #7a9bb3;
    border-radius: 8px;
    padding: 4px;
    transition: background 0.3s ease;

    svg {
      transition: all 0.3s ease;
    }

    &.isActive {
      background: #e8f0fe;
    }
    &.add-container {
      svg {
        fill: colors.$medium-green;

        &:hover {
          transform: rotate(90deg);
        }
      }
    }

    &.edit-container {
      svg {
        fill: #95a5a6;

        &:hover {
          transform: scaleX(-1);
        }
      }
    }
    &:not(.isActive):hover{
      background: #eff2f8;
    }
  }
}
</style>
