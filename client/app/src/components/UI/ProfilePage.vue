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
      </div>
    </div>

    <div class="tab-content">
      <div v-if="!nav.chooseSettings">
        <BookingContainer>
        </BookingContainer>
      </div>
      <div v-if="nav.chooseSettings">
        Настройки профиля
      </div>
    </div>
  </div>

</template>
<script>
import BookingContainer from "@/components/bookings/BookingContainer.vue";
export default {
  components: {
    BookingContainer
  },
  name: 'AppProfile',
  data(){
    return {
      nav:{
        chooseOrders: true,
        chooseSettings: false,
      }
    }
  },
  methods: {
    chooseNav(type){
      this.nav.chooseOrders = type === 'orders'
      this.nav.chooseSettings = type === 'settings'
    },
  },
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/profilepage.sass'
</style>