<template>
    <div>
        <div class="changePassword">

        </div>
        <ul>
            <li v-for="device in devices" :key="device.id">
                {{ device.id }}
                {{ device.deviceFingerprint }}
                {{ device.userAgent }}
            </li>
        </ul>
    </div>
</template>
<script setup>
import UserService from '@/services/UserService';
import { ref, onMounted } from 'vue';

const devices = ref([])
const fetchUserDevices = async() => {
    const response = await UserService.getUserAgent();
    devices.value = response.data;
}

onMounted(() => {
    fetchUserDevices();
})
</script>
<style>
    
</style>