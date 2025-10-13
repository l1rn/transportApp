<template>
  <div>
    <div class="custom-main-container">
      <div class="custom-main">
        <button
          :class="{active:chooseAllUsers}"
          @click="chooseNav('allusers')"
        >
          Все пользователи
        </button>
        <button
          :class="{active:chooseAllBookings}"
          @click="chooseNav('allbookings')"
        >
          Все брони пользователей
        </button>
        <button
          :class="{active:chooseAllRoutes}"
          @click="chooseNav('allroutes')"
        >
          Маршруты
        </button>
        <button
          :class="{active:allrouteslooks}"
          @click="chooseNav('allrouteslooks')"
        >
          Все маршруты
        </button>
      </div>
      <div v-if="chooseAllUsers">
        <AdminAllUsers />
      </div>
      <div v-if="chooseAllBookings">
        <AdminAllBookings />
      </div>
      <div v-if="chooseAllRoutes">
        <AdminAllRoutes />
      </div>
      <div v-if="chooseAllRoutesForLook">
        <SearchRoutes />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue';
import AdminAllUsers from './AdminAllUsers.vue';
import AdminAllBookings from './AdminAllBookings.vue';
import AdminAllRoutes from './AdminAllRoutes.vue';
import SearchRoutes from '../SearchRoutesView.vue';
let chooseAllUsers = ref(false);
let chooseAllBookings = ref(false);
let chooseAllRoutes = ref(false);
let chooseAllRoutesForLook = ref(false)

const chooseNav = (type) => {
    chooseAllBookings.value = type === 'allbookings'
    chooseAllRoutes.value = type === 'allroutes'
    chooseAllUsers.value = type === 'allusers'
    chooseAllRoutesForLook.value = type === 'allrouteslooks'
}

</script>

<style scoped lang="sass">
@import "@/assets/styles/adminObjects/admin-panel";
</style>