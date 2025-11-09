<template>
  <div>
    <div class="custom-main-container">
      <div class="custom-main">
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
          :class="{ active: allrouteslooks }"
          @click="chooseNav('allrouteslooks')"
        >
          Все маршруты
        </button>
      </div>
      <div v-if="chooseAllUsers">
        <AdminAllUsersView />
      </div>
      <div v-if="chooseAllBookings">
        <AdminAllBookingsView />
      </div>
      <div v-if="chooseAllRoutes">
        <AdminAllRoutesView />
      </div>
      <div v-if="chooseAllRoutesForLook">
        <SearchRoutesView />
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
let chooseAllUsers = ref(false);
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
$primary-color: #d19a01;

* {
  font-family: Montserrat, "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.custom-main {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  white-space: nowrap;
  margin-bottom: 2rem;

  button {
    padding: 0.6rem 2rem;
    border-radius: 8px;
    border: 0;
    background: linear-gradient(
      -30deg,
      darken($primary-color, 2%),
      lighten($primary-color, 3%)
    );
    transition: all 0.3s ease;
    color: #f5f0fd;
    font-weight: 600;
    max-width: 300px;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);
    }
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
