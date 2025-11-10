<template>
  <div>
    <div class="admin-panel-wrapper">
      <div class="admin-panel-container">
        <div class="button-container">
          <button :class="{ active: chooseAllUsers }" @click="chooseNav('allusers')">
            Все пользователи
          </button>
          <button :class="{ active: chooseAllBookings }" @click="chooseNav('allbookings')">
            Все брони пользователей
          </button>
          <div class="route-button-wrapper">
            <button :class="{ active: chooseAllRoutes }" @click="chooseNav('allroutes')">
              Маршруты
            </button>
            <template v-if="chooseAllRoutes">
              <div class="mode-container">
                <span 
                @click="selectType('add')" 
                class="add-container"
                :class="{ isActive: typeSelected === 'add' }">
                  <svg :class="{ isActive: typeSelected === 'add' }" xmlns="http://www.w3.org/2000/svg" height="24px"
                    viewBox="0 -960 960 960" width="24px" fill="#e3e3e3">
                    <path d="M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z" />
                  </svg>
                </span>
                <span 
                @click="selectType('edit')" 
                class="edit-container"
                :class="{ isActive: typeSelected === 'edit' }">
                  <svg xmlns="http://www.w3.org/2000/svg" height="24px"
                    viewBox="0 -960 960 960" width="24px" fill="#e3e3e3">
                    <path
                      d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z" />
                  </svg>
                </span>
                <span 
                @click="selectType('delete')" 
                class="delete-container"
                :class="{ isActive: typeSelected === 'delete' }">
                  <svg xmlns="http://www.w3.org/2000/svg" height="24px"
                    viewBox="0 -960 960 960" width="24px" fill="#e3e3e3">
                    <path
                      d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z" />
                  </svg>
                </span>
              </div>
            </template>
          </div>
          <button :class="{ active: chooseAllRoutesForLook }" @click="chooseNav('allrouteslooks')">
            Поиск маршрутов
          </button>
        </div>
        <div class="content-container">
          <template v-if="chooseAllUsers">
            <AdminAllUsersView />
          </template>
          <template v-if="chooseAllBookings">
            <AdminAllBookingsView />
          </template>
          <template v-if="chooseAllRoutes">
            <AdminAllRoutesView :mode="typeSelected" />
          </template>
          <template v-if="chooseAllRoutesForLook">
            <SearchRoutesView />
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import AdminAllUsersView from "@/components/molecule/admin/AdminAllUsersView.vue";
import AdminAllBookingsView from "@/components/molecule/admin/AdminAllBookingsView.vue";
import AdminAllRoutesView from "@/components/molecule/admin/AdminAllRoutesView.vue";
import SearchRoutesView from "@/components/page/SearchRoutesView.vue";
import { ActionType } from "@/shared/types/component";

const typeSelected = ref<ActionType>("add");

const chooseAllUsers = ref(true);
const chooseAllBookings = ref(false);
const chooseAllRoutes = ref(false);
const chooseAllRoutesForLook = ref(false);

const chooseNav = (type: string) => {
  chooseAllBookings.value = type === "allbookings";
  chooseAllRoutes.value = type === "allroutes";
  chooseAllUsers.value = type === "allusers";
  chooseAllRoutesForLook.value = type === "allrouteslooks";
};

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

* {
  font-family: Montserrat, "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.admin-panel-wrapper {
  @include mixins.display-center();
}

.admin-panel-container {
  display: flex;
  width: 82.5%;
  gap: 1rem;

  .button-container {
    @include mixins.display-column();
    background: #f5f8fa;
    padding: 1rem 1.25rem;
    gap: 0.5rem;
    border-radius: 8px;
    height: 100%;

    button {
      @include mixins.button-clear(white, #7a9bb3);
      border: 2px solid #7a9bb3;
      padding: 0.6rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      font-size: 15px;
      transition: all 0.3s ease;
      word-break: keep-all;
      text-align: center;
      width: 100%;

      &:hover {
        background: #f1f9fe;
      }

      &.active {
        transform: translateX(5px);
        background: #7a9bb3;
        color: white;
      }
    }

    .route-button-wrapper {
      display: flex;
      position: relative;

      .mode-container {
        @include mixins.display-column();
        gap: 0.25rem;
        position: absolute;
        top: -100%;
        right: -22.5%;

        span {
          display: flex;
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

          &.delete-container {
            svg {
              fill: colors.$medium-red;

              &:hover {
                transform: rotate(35deg);
              }
            }
          }
        }
      }
    }
  }

  .content-container {
    height: 100%;
    width: 100%;
  }
}

@media screen and (max-width: 911px) {
  .custom-main {
    flex-direction: column;

    button {
      width: 100%;
      margin-bottom: 0.5rem;
      white-space: normal;
    }
  }
}
</style>
