<template>
  <div>
    <div class="admin-panel-wrapper">
      <div class="admin-panel-container">
        <div class="button-container">
          <button
            :class="{ active: chooseAllUsers }"
            @click="chooseNav('allusers')"
          >
            Все пользователи
          </button>
          <button
            :class="{ active: chooseAllBookings }"
            @click="chooseNav('allbookings')"
          >
            Все брони пользователей
          </button>
          <button
            :class="{ active: chooseAllRoutes }"
            @click="chooseNav('allroutes')"
          >
            Маршруты
          </button>
          <button
            :class="{ active: chooseAllRoutesForLook }"
            @click="chooseNav('allrouteslooks')"
          >
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
            <AdminAllRoutesView />
          </template>
          <template v-if="chooseAllRoutesForLook">
            <SearchRoutesView />
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
import AdminAllUsersView from "@/components/molecule/admin/AdminAllUsersView.vue";
import AdminAllBookingsView from "@/components/molecule/admin/AdminAllBookingsView.vue";
import AdminAllRoutesView from "@/components/molecule/admin/AdminAllRoutesView.vue";
import SearchRoutesView from "@/components/page/SearchRoutesView.vue";
let chooseAllUsers = ref(true);
let chooseAllBookings = ref(false);
let chooseAllRoutes = ref(false);
let chooseAllRoutesForLook = ref(false);

const chooseNav = (type) => {
  chooseAllBookings.value = type === "allbookings";
  chooseAllRoutes.value = type === "allroutes";
  chooseAllUsers.value = type === "allusers";
  chooseAllRoutesForLook.value = type === "allrouteslooks";
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

.admin-panel-container{
  display: flex;
  width: 82.5%;
  gap: 1rem;
  .button-container {
    @include mixins.display-column();
    background: #F5F8FA;
    padding: 1rem 1.25rem;
    gap: 0.5rem;
    border-radius: 8px;
    height: 100%;
    button {
      @include mixins.button-clear(white, #7A9BB3);
      border: 2px solid #7A9BB3;
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
      &.active{
        transform: translateX(5px);
        background: #7A9BB3;
        color: white;
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
