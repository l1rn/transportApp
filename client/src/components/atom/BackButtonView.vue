<template>
  <div class="button-container">
    <div class="pos">
      <button
        class="button-back-header" 
        @click="$router.replace('/home')"
      >
        Назад
      </button>
    </div> 
  </div>
</template>
<style lang="scss">
@import "../../assets/styles/static/mixin.d.scss";

.button-container{
    .button-back-header{
    @include button-clear($back: $primary-blue, $color: $white);
    padding: 0.8rem 3rem;
    font-size: 16px;
    border-radius: 0.5rem;
    transition: all 0.3s ease;
    font-weight: 600;
    &:hover{
        transform: translateY(-2px);
        box-shadow: 0 6px 12px rgba(0,0,0,0.15);
        background: darken($primary-blue, 5%);
    }         
  }
}
</style>