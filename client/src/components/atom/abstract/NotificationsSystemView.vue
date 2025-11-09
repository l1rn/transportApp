<template>
  <transition-group
    name="notification"
    tag="div"
    class="notifications-wrapper"
  >
    <div
      v-for="(notification, index) in notifications"
      :key="index"
      :class="['notification', notification.type]"
    >
      {{ notification.message }}
    </div>
  </transition-group>
</template>

<script setup lang="ts">
import type { Notification } from '@/shared/plugins/notifications';
import { inject, Ref } from 'vue';

const notifications = inject<Ref<Notification[]>>('notifications')!;
</script>
<style scoped>
* {
  font-family: Montserrat, 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
}

.notifications-wrapper {
  z-index: 100;
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 10000;
  max-width: 300px;
}

.notification {
  padding: 15px;
  margin: 10px 0;
  border-radius: 8px;
  color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  opacity: 0.95;
  transition: all 0.3s ease;
}

.success {
  background: #10b981;
}

.error {
  background: #ef4444;
}

.notification-enter-from,
.notification-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.notification-leave-active {
  position: absolute;
}
</style>