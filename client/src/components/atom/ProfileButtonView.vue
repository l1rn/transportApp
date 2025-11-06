<template>
  <div
    class="profile-container"
  >
    <button
      class="custom-profile-btn"
      @click.stop="modalStore.toggle('profile-menu')"
    >
      Профиль
    </button>
    <transition name="slide-fade">
      <template v-if="modalStore.isOpen('profile-menu')">
          <div
            class="profile-menu">
              <ul ref="profileMenuRef"> 
                <template v-if="isAuthenticated">
                  <li @click.stop="handleProfileClick">
                    <span>👤</span> Профиль
                  </li>
                  <li @click.stop="logoutProfileMenu">
                    <span>🚪</span> Выход
                  </li>
                </template>
                <template v-else>
                  <li 
                  @click.stop="openForm('login')" 
                  :class="{'auth': !isAuthenticated}">
                    <span>🔑</span> Авторизация
                  </li>
                </template>
              </ul>
          </div>
        </template>        
    </transition>
  </div>
</template>
<script setup lang="ts">
import { useAuthForms } from '@/composable/useAuthForms';
import { useConditionalClickOutside } from '@/composable/useConditionalClickOutside';
import { authorizationService } from '@/shared/services/authorizationService';
import { userService } from '@/shared/services/userService';
import { useAuthStore } from '@/shared/stores/useLoginStore';
import { useModalStore } from '@/shared/stores/useModalStore';
import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const { openForm } = useAuthForms();

const router = useRouter();

const modalStore = useModalStore();
const profileMenuRef = ref<HTMLElement | null>(null);

const authStore = useAuthStore();
const { isAuth: isAuthenticated } = storeToRefs(authStore);

const handleProfileClick = () => {
  router.push('/profile');
  modalStore.close('profile-menu');
}

const logoutProfileMenu = async() => {
  try {
    modalStore.close('profile-menu');
    await authorizationService.logoutUser();
    authStore.logout();
    router.push("/home");
  }
  catch(error){
    console.error(error);
  }
}

onMounted(async() => {
  const response = await userService.getMyStatus();
  if(response.data === true && response.status === 200){
    authStore.login();
  }
})

useConditionalClickOutside(
  profileMenuRef,
  () => modalStore.isOpen("profile-menu"),
  () => modalStore.close("profile-menu")  
)
</script>
<style scoped lang="scss">
@use "../../assets/styles/static/color" as colors;
@use "../../assets/styles/static/mixin" as mixins;

*{
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
}

.custom-profile-btn {
  @include mixins.button-clear($back: #e7eff3);
  font-weight: 600;
  font-size: 18px;
  padding: 0.75rem 1.5rem;
  border-radius: 20px;
  color: colors.$primary-blue;
  transition: all 0.3s ease;
}

.custom-profile-btn:hover {
  transform: translateY(-2px);
}

.profile-menu {
  position: relative;
  ul {
    position: absolute;
    list-style-type: none;
    width: 120%;
    margin: 0;
    padding: 0;
    right: 0;
    top: 8px;
    li {
      background: #f1f4f5;
      padding: 0.5rem 0.75rem;
      display: flex;
      align-items: center;
      cursor: pointer;
      gap: 0.25rem;
      border-radius: 8px;
      font-weight: 600;
      &:hover {
        background: #e7eaea;
      }

      &:not(.auth):first-child{
        border-radius: 8px 8px 0 0;
      }
      &:not(.auth):last-child{
        border-radius: 0 0 8px 8px;
      }
    }
  }
}

</style>