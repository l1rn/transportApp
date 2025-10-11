<template>
  <div
    ref="profileDropdown"
    class="profile-container"
  >
    <button
      class="custom-profile-btn"
      @click="toggleProfileMenu"
    >
      <div class="profile-content">
        <span class="profile-text">Профиль</span>
      </div>
    </button>
    <transition name="slide-fade">
      <div
        v-if="modalStore.isOpen('profile-menu')"
        ref="profileMenuRef"
        class="profile-menu"
      >
        <slot name="menu-items">
          <div
            v-if="isAuthenticated"
            class="menu-item"
            @click="handleMenuItemClick('orders')"
          >
            📖 Мои заказы
          </div>
          <div
            v-if="isAuthenticated"
            class="menu-item"
            @click="logoutProfileMenu();
                    $router.push({path:'/'});"
          >
            🚪 Выйти
          </div>
          <div
            v-else
            class="menu-item"
            @click.stop="openForm('login')"
          >
            🔑 Авторизация
          </div>
        </slot>
      </div>
    </transition>
  </div>
</template>
<script setup>
import { useAuthForms } from '@/composable/useAuthForms';
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { authorizationService } from '@/services/authorizationService';
import { useLoginStore } from '@/stores/authStore';
import { useModalStore } from '@/stores/modalStore';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';

const loginStore = useLoginStore();
const { logined } = storeToRefs(loginStore)
const isAuthenticated = computed(() => logined.value);

const { openForm } = useAuthForms();

const modalStore = useModalStore();
const profileMenuRef = ref<HTMLElement | null>(null);

const toggleProfileMenu = () => {
  modalStore.toggle("profile-menu");
}

const logoutProfileMenu = () => {
  try {
    authorizationService.logoutUser();
  }
  catch(error){
    console.error(error);
  }
}

onMounted(() => {
  loginStore.initLoginState()
})

useConditionalClickOutside(
  profileMenuRef,
  () => modalStore.isOpen("profile-menu"),
  () => modalStore.close("profile-menu")  
)
</script>
<style scoped>
*{
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
}
.profile-container {
  position: relative;
  margin-left: 1rem;
}

.custom-profile-btn {
  background: none;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.custom-profile-btn:hover {
  transform: translateY(-2px);
}

.profile-content {
  align-items: center;
  gap: 0.5rem;
  width: 9rem;
  display: flex;
  justify-content: center;
  height: 3rem;
  border-radius: 16px;
  background: #e7eff3;
}

.profile-text {
  font-family: 'Montserrat', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  color: #1778dc;

}

.profile-menu {
  position: absolute;
  right: 0;
  top: 100%;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  min-width: 200px;
  z-index: 1000;
  margin-top: 0.5rem;
}

.menu-item {
  padding: 0.75rem 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}


.menu-item:first-child {
  border-radius: 8px 8px 0 0;
}

.menu-item:last-child {
  border-radius: 0 0 8px 8px;
}

</style>