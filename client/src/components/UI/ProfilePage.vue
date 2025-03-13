<template>
  <div class="profile-page">
    <BackbuttonToHome />
    <div class="header-profile">
      <div class="header-profile__title">
        <h1>Профиль</h1>
      </div>
    </div>

    <div class="main-container-profile">
      <div class="nav-tabs">
        <button
          class="nav-link"
          :class="{ active: nav.chooseOrders}"
          @click="chooseNav('orders')"
        >
          Мои заказы
        </button>
        <button
          class="nav-link"
          :class="{ active: nav.chooseSettings}"
          @click="chooseNav('settings')"
        >
          Настройки
        </button>
        <button
          v-if="hasRoleAdmin"
          class="nav-link"
          :class="{ active: nav.chooseModeration}"
          @click="chooseNav('moderation')"
        >
          Модерирование
        </button>
        <button
          v-else
          class="nav-link"
        >
          Информация о пользователе
        </button>
      </div>
    </div>

    <div class="tab-content">
      <div v-if="nav.chooseOrders">
        <BookingCard />
      </div>
      <div v-if="nav.chooseSettings">
        Настройки профиля
      </div>
      <div v-if="nav.chooseModeration">
        <AdminPanel />
      </div>
    </div>
  </div>
</template>
<script setup>
import BackbuttonToHome from "./BackbuttonToHome.vue";
import { onMounted, ref  } from "vue";
import AdminPanel from "../admin/AdminPanel.vue";

const checkTokenInProfile = async() => {
  try{
    await BookingService.checkRefreshToken;
  }
  catch(error){
    console.log(error);
  }
}

const checkAdminRole = () => {
  try{
    const role = getRoleFromToken();
    hasRoleAdmin.value = role === 'ROLE_ADMIN';
  }
  catch(error){
    console.error('Role check failed:', error);
    hasRoleAdmin.value = false; 
  }
};
const hasRoleAdmin = ref(false);
onMounted(() =>{
  checkTokenInProfile();
  checkAdminRole();
})
</script>
<script>
import BookingCard from "@/components/bookings/BookingCard.vue";
import { getRoleFromToken } from "@/services/api";
import BookingService from "@/services/BookingService";
export default {
  name: 'AppProfile',
  components: {
    BookingCard
  },
  data(){
    return {
      nav:{
        chooseOrders: true,
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