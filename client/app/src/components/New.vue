<!-- eslint-disable vue/multi-word-component-names -->
<template>
   
  <div class="container">
    <input v-model="routeId">
    <button @click="addbook">sub</button>
  </div>
  
</template>
<script>
import BookingService from '@/services/BookingService'
export default {
  name: 'AppRoutes',
  data(){
      return{
          routeId: 1
      }
  },
  methods:{
      async addbook(){
          try{
              const token = localStorage.getItem('token');
              const response = await BookingService.addBooking(this.routeId, {
                  headers:{
                      'Authorization': `Bearer ${token}`,
                      'Content-Type': 'application/json',
                  },
              });
              console.log(response.data);
          }
          catch(error){
              console.log(error.message);
          } 
      }
  },
}
</script>
<style lang="">
  
</style>