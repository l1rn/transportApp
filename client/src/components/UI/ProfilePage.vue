<template>
  <div class="profile-page">
    <BackbuttonToHome />
    <div class="header-profile">
      <div class="header-profile__title">
        <h1>Профиль - {{ userData.username }}</h1>
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
          v-if="!!hasRoleAdmin"
          class="nav-link"
          :class="{ active: nav.chooseModeration}"
          @click="chooseNav('moderation')"
        >
          Модерирование
        </button>
        
      </div>
    </div>

    <div class="tab-content">
      <div v-if="nav.chooseOrders">
        <BookingCard />
      </div>
      <div v-if="nav.chooseSettings">
        <UserSettings />
      </div>
      <div v-if="nav.chooseModeration">
        <AdminPanel />
      </div>
      
    </div>
  </div>
</template>
<script setup>
import UserSettings from '@/components/UI/usercomponents/UserSettings.vue'
import BackbuttonToHome from "./BackbuttonToHome.vue";
import { onMounted, onUnmounted, ref, watch  } from "vue";
import AdminPanel from "../admin/AdminPanel.vue";
import { useRoleStore } from "@/stores/roleStore";

const roleStore = useRoleStore();
const userStore = useDataSource();

const { userData } = storeToRefs(userStore);
let { currentRole } = storeToRefs(roleStore);

const getDevices = async() => {
  await userStore.getUserData()
}

const checkTokenInProfile = async() => {
  try{
    await BookingService.checkRefreshToken();
  }
  catch(error){
    console.log(error);
  }
}

const hasRoleAdmin = ref(false)

const checkAdminRole = async() => {
  try{
    await roleStore.getRole()
    hasRoleAdmin.value = currentRole.value === "ADMIN"
  }catch(error){
    console.error("НЕ АДМИН", error)
    hasRoleAdmin.value = false;
  }  
};
watch(hasRoleAdmin, (newValue) => {
  console.log('Admin status changed:', newValue);
})
onMounted(() =>{
  checkTokenInProfile();
  checkAdminRole();
  getDevices()
})
onUnmounted(() => {
  hasRoleAdmin.value = false
})
</script>
<script>
import BookingCard from "@/components/bookings/BookingCard.vue";
import BookingService from "@/services/BookingService";
import { storeToRefs } from "pinia";
import { useDataSource } from '@/stores/userDataStore';
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