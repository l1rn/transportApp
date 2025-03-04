<template lang="">
  <div>
    <route-container :tickets="ticketsData" />
  </div>
</template>
<script>
import RouteContainer from './customRoutes/RouteContainer.vue';

export default {
  name: 'page-routes',
  components:{
    'route-container': RouteContainer
  },
  data() {
  return {
    
  }
}
}
</script>
<style lang="">
  
</style>