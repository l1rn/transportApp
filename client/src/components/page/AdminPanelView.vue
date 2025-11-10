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
          </div>
          <button :class="{ active: chooseAllRoutesForLook }" @click="chooseNav('allrouteslooks')">
            Поиск маршрутов
          </button>
        </div>
        <div class="content-container">
          <template v-if="chooseAllUsers">
            <AsyncAdminAllUsersView />
          </template>
          <template v-if="chooseAllBookings">
            <AsyncAdminAllBookingsView />
          </template>
          <template v-if="chooseAllRoutes">
            <AsyncAdminAllRoutesView />
          </template>
          <template v-if="chooseAllRoutesForLook">
            <AsyncSearchRoutesView />
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { defineAsyncComponent, ref } from "vue";

const AsyncAdminAllUsersView = defineAsyncComponent(() => 
  import("@/components/organism/admin/AdminAllUsersView.vue")
);
const AsyncAdminAllBookingsView = defineAsyncComponent(() => 
  import("@/components/organism/admin/AdminAllBookingsView.vue")
);
const AsyncAdminAllRoutesView = defineAsyncComponent(() => 
  import("@/components/organism/admin/AdminAllRoutesView.vue")
);
const AsyncSearchRoutesView = defineAsyncComponent(() => 
  import("@/components/page/SearchRoutesView.vue")
);

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

</script>

<style scoped lang="scss">
@use "../../assets/styles/static/mixin" as mixins;
@use "../../assets/styles/static/color" as colors;

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
