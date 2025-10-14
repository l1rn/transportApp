import NotificationsSystemView from "@/components/atom/NotificationsSystemView.vue";
import { ComponentPublicInstance, createApp, ref } from "vue";

export type NotificationType = 'success' |  'error';

export interface Notification {
  id: number;
  type: NotificationType;
  message: string;  
}

const notificationInstance = ref<ComponentPublicInstance | null>(null);
const notifications = ref<Notification[]>([]);
let isMounted = false;

const createNotificationApp = () => {
    const app = createApp(NotificationsSystemView, {
        notifications
    });

    const container = document.createElement('div');
    document.body.appendChild(container);
    container.id = 'notifications-container';
    notificationInstance.value = app.mount(container);
    isMounted = true;

    return notificationInstance;
};

const showNotification = (
        type: NotificationType, 
        message: string, 
        duration: number = 3000
    ): void => {
    if(!notificationInstance.value){
        createNotificationApp();
    }

    const id = Date.now();
    notifications.value.push({ id, type, message});
    
    setTimeout(() => {
        notifications.value = notifications.value.filter(n => n.id !== id);
    }, duration);
};

const notification = {
    success: (message: string, duration?: number) =>
        showNotification('success', message, duration),

    error: (message: string, duration?: number) =>
        showNotification('error', message, duration),

    show: showNotification
}

export default notification