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
*{
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif

}
.button-container{
    display: flex;
    justify-content: center;
    align-self: flex-start;
    margin: 1rem 0.5rem;
    .button-back-header{
    padding: 0.8rem 3rem;
    background: #007bff;
    color: white;
    border: none;
    border-radius: 0.5rem;
    cursor: pointer;
    transition: all 0.3s ease;
    margin-bottom: 2rem;
    font-weight: 500;
    &:hover{
        transform: translateY(-2px);
        box-shadow: 0 6px 12px rgba(0,0,0,0.15);
        background: darken(#007bff, 5%);
    }         
}
}


</style>