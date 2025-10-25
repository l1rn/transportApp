<template>
  <div class="main-header-wrapper">
    <div class="main-header-container">
        <div class="navbar-subheader">
          <div 
          style="cursor: pointer;" 
          class="brand" 
          @click="$router.replace('/home')">
            ololotravel
          </div>
          <div class="profile-header-custom">
            <ProfileButtonView />
          </div>
        </div>
    </div>
  </div>
</template>
<script setup>
import ProfileButtonView from '../atom/ProfileButtonView.vue';


</script>

<style scoped lang="scss">
.brand{
    font-family: $font-family-base;
    font-weight: 700;
    font-size: 1.2rem;
    color: $primary;
    position: relative;
    &::after {
        content: '🚀';
        top: -1.2rem;
        position: absolute;
    }
}
    
</style>