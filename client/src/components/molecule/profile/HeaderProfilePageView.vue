<template>
    <div class="header-wrapper">
      <div class="header-container">
        <div class="title-container">
          <span>👤 Профиль: {{ props.username }}</span>
          <span>💵 Баланс: {{ props.balance }} ₽</span>
        </div>
        <div class="space"></div>
        <div class="nav-tabs">
          <button
            class="nav-link"
            :class="{ active: modalStore.isOpen('profile-page-bookings')}"
            @click="openForm('profile-page-bookings')"
          >
            Мои заказы
          </button>
          <button
            class="nav-link"
            :class="{ active: modalStore.isOpen('profile-page-settings')}"
            @click="openForm('profile-page-settings')"
          >
            Настройки
          </button>  
          <button @click="handlePayments">
            Мои платежи
          </button>
          <button
            v-if="!!props.hasRoleAdmin"
            class="nav-link"
            :class="{ active: modalStore.isOpen('profile-page-admin-panel')}"
            @click="openForm('profile-page-admin-panel')"
          >
            Модерирование
          </button>
        </div>
      </div>
    </div>
</template>
<script setup lang="ts">
import { useProfilePage } from '@/composable/useProfilePage';
import { useModalStore } from '@/shared/stores/useModalStore';
import { useRouter } from 'vue-router';

const props = defineProps<{
    username: string;
    balance: number | null;
    hasRoleAdmin: boolean;
}>();

const modalStore = useModalStore();

const { openForm } = useProfilePage();
const router = useRouter();

const handlePayments = () => {
  const routeData = router.resolve({name: "payment"})
  window.open(routeData.href, '_blank')
}
</script>
<style lang="scss">
@use "../../../assets/styles/static/mixin" as mixins;
@use "../../../assets/styles/static/color" as colors;

.header-wrapper{
    display: flex;
    justify-content: center;
    width: 100%;
    .header-container {
      width: 80%;
      padding: 1rem 1.5rem;
      margin: 1rem 0;
      border-radius: 16px;
      box-shadow: colors.$input-shadow;
      background: colors.$white;
      .title-container{
        @include mixins.display-column();
        gap: 0.75rem;
        span {
          text-transform: uppercase;
          letter-spacing: 0.05rem;
          font-size: 1.5rem;
        }
      }
      .space {
        width: 100%;
        background: colors.$light-grey;
        height: 2px;
        margin: 1.5rem 0 0 0;
      }
      .nav-tabs {
        button {
          @include mixins.button-clear(colors.$main-white);
          font-size: 1.25rem;
          padding: 0.5rem 1rem;
        }
        .active {
          border-bottom: 2px solid colors.$primary-blue;
        }
      }
    } 
  }    
</style>