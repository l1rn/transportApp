<template>
  <div class="header-container-custom">
    <div class="navbar-custom d-flex flex-row align-items-center justify-content-between">
      <b-nav @click="this.$router.replace('/profile')">
        <BNavItem>
          <h1>Профиль</h1>
        </BNavItem>
      </b-nav>

      <BButton size="md" class="" @click="this.$router.replace('/home')">Назад</BButton>
    </div>
    <div class="container d-flex flex-column align-items-center">
      <b-tabs no-fade>
        <b-tab title="Мои заказы">
        </b-tab>
        <b-tab title="Настройки">
        </b-tab>
      </b-tabs>
    </div>
    <div class="container d-flex flex-column align-items-center">
      <b-navbar>
        <BookingContainer></BookingContainer>
      </b-navbar>
    </div>
  </div>


</template>
<script>
import BookingContainer from "@/components/bookings/BookingContainer.vue";
import {BNavbar, BTab, BTabs, BButton, BNav, BNavItem} from "bootstrap-vue-next";
export default {
  components: {
    'b-nav': BNav,
    'b-navbar': BNavbar,
    'b-tabs': BTabs,
    'b-tab': BTab,
    BNavItem,
    BButton,
    BookingContainer
  },
    name: 'AppProfile',
  methods: {

  }
}
</script>
<style>

</style>