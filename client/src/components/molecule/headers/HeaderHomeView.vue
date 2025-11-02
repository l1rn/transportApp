<template>
  <div class="main-header-wrapper">
    <div class="main-header-container">
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
</template>
<script setup>
import ProfileButtonView from '@/components/atom/ProfileButtonView.vue';


</script>

<style scoped lang="scss">
@use "../../../assets/styles/static/color" as colors;
@use "../../../assets/styles/static/mixin" as mixins;

.main-header-wrapper {
    .main-header-container{
        display: flex;
        justify-content: space-evenly;
        align-items: center;
        background: linear-gradient(135deg, lighten(#007bff, 15%), darken(#007bff, 5%));
        padding: 2rem 0;

        .brand{
            font-weight: 700;
            font-size: 1.2rem;
            color: colors.$white;
            position: relative;
            &::after {
                content: '🚀';
                top: -1.2rem;
                position: absolute;
            }
        }         
    }
}   
</style>