<script>
import {BAvatar} from "bootstrap-vue-next";

export default {
  name: "CustomProfile",
  components: {
    BAvatar
  },
  props: {
    avatarUrl:{
      type: String,
      default: ''
    },
    profileText:{
      type: String,
      default: 'Профиль'
    },
    isAuthenticated: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isProfileMenuOpen: false,
      defaultAvatarUrl:BAvatar,
    }
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  methods: {
    toggleProfileMenu() {
      this.isProfileMenuOpen = !this.isProfileMenuOpen;
    },
    async handleMenuItemClick(action){
      switch (action) {
        case "orders": 
          this.$router.push({path:'/profile'});
          break;
        case "auth":
          this.$emit('open-auth');
          break;
        case "logout":
          this.$emit('logout');
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          this.$router.push('/');
          break;
      }
      this.closeMenu();
    },
    closeMenu() {
      this.isProfileMenuOpen = false;
    },
    handleClickOutside(event) {
      if(!this.$refs.profileDropdown?.contains(event.target)) {
        this.closeMenu();
      }
    },
  }
}
</script>

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
        <BAvatar size="2rem" />
        <span class="profile-text">Профиль</span>
      </div>
    </button>
    <transition name="slide-fade">
      <div
        v-if="isProfileMenuOpen"
        class="profile-menu"
      >
        <slot name="menu-items">
          <div
            v-if="isAuthenticated"

            class="menu-item"
            @logined-auth="handleLogout"
            @click="handleMenuItemClick('orders')"
          >
            📖 Мои заказы
          </div>
          <div
            v-if="isAuthenticated"
            class="menu-item"
            @click="handleMenuItemClick('logout');
                    $router.push({path:'/'});"
          >
            🚪 Выйти
          </div>
          <div
            v-else
            class="menu-item"
            @click="handleMenuItemClick('auth');
                    $router.push({path:'/'});"
          >
            🔑 Авторизация
          </div>
        </slot>
      </div>
    </transition>
  </div>
</template>

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