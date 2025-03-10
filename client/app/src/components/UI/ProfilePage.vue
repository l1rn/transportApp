<template>
  <div class="profile-page">
    <button @click="this.$router.replace('/home')" class="button-back">Назад</button>
    <div class="header-profile">
      <div class="header-profile__title">
        <h1>Профиль</h1>
      </div>
    </div>

    <div class="main-container-profile">
      <div class="nav-tabs">
        <button class="nav-link"
        :class="{ active: nav.chooseOrders}"
        @click="chooseNav('orders')">
          Мои заказы
        </button>
        <button class="nav-link"
        :class="{ active: nav.chooseSettings}"
        @click="chooseNav('settings')">
          Настройки
        </button>
        <button
        v-if="hasRoleAdmin"
        class="nav-link"
        :class="{ active: nav.chooseModeration}"
        @click="chooseNav('moderation')">
          Модерирование
        </button>
      </div>
    </div>

    <div class="tab-content">
      <div v-if="nav.chooseOrders">
        <BookingCard>
        </BookingCard>
      </div>
      <div v-if="nav.chooseSettings">
        Настройки профиля
      </div>
      <div v-if="nav.chooseModeration">
        <AdminPanel>
        </AdminPanel>
      </div>
    </div>
  </div>

</template>
<script setup>
import { onMounted, ref  } from "vue";
import AdminPanel from "../admin/AdminPanel.vue";
const checkAdminRole = () => {
    hasRoleAdmin.value = getRoleFromToken() === 'ROLE_ADMIN';
    console.log(hasRoleAdmin.value);
};
const hasRoleAdmin = ref(false);
onMounted(() =>{
  checkAdminRole();
})
</script>
<script>
import BookingCard from "@/components/bookings/booking/BookingCard.vue";
import { getRoleFromToken } from "@/services/api";
export default {
  components: {
    BookingCard
  },
  name: 'AppProfile',
  data(){
    return {
      nav:{
        chooseOrders: false,
        chooseSettings: false,
        chooseModeration: false,
      }
    }
  },
  methods: {
    chooseNav(type){
      this.nav.chooseOrders = type === 'orders'
      this.nav.chooseSettings = type === 'settings'
      this.nav.chooseModeration = type === 'moderation'
    },

  },
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/profilepage.sass'
</style>