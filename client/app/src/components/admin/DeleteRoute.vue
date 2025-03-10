<template>
    <div>
        <div>
            <label for="routeID"></label>
            <input type="text" name="" id="routeID" v-model="routeID">
            <button @click="deleteByBookingId">Удалить</button>
        </div>
    </div>
</template>
<script setup>
import AdminService from '@/services/AdminService';
import {ref} from 'vue'
const routeID = ref();
const deleteByBookingId = async() => {
    const response = await AdminService.deleteRoute(routeID.value);
}

</script>
<script>
export default {
    
}
</script>
<style lang="">
    
</style>