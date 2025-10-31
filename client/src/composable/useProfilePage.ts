import { useModalStore } from "@/shared/stores/useModalStore"
import { computed } from "vue";

export const useProfilePage = () => {
    const modalStore = useModalStore();

    const currentForm = computed(() => {
        if(modalStore.isOpen('profile-page-bookings')) return 'profile-page-bookings';
        if(modalStore.isOpen('profile-page-settings')) return 'profile-page-settings';
        if(modalStore.isOpen('profile-page-admin-panel')) return 'profile-page-admin-panel';
        return null;
    })

    const switchForms = () => {
        if(currentForm.value === 'profile-page-bookings'){
            modalStore.close('profile-page-admin-panel');
            modalStore.close('profile-page-settings');
            modalStore.open('profile-page-bookings');
        }
        else if(currentForm.value === 'profile-page-settings'){
            modalStore.close('profile-page-admin-panel');
            modalStore.close('profile-page-bookings');
            modalStore.open('profile-page-settings');
        }
        else if(currentForm.value === 'profile-page-admin-panel'){
            modalStore.close('profile-page-bookings');
            modalStore.close('profile-page-settings');
            modalStore.open('profile-page-admin-panel');
        }
    }

    const openForm = (
        form: 'profile-page-bookings' |
        'profile-page-settings' |
        'profile-page-admin-panel'
    ) => {
        modalStore.close('profile-page-bookings');
        modalStore.close('profile-page-settings');
        modalStore.close('profile-page-admin-panel');
        modalStore.open(form);
    }

    return {
        currentForm,
        switchForms,
        openForm
    }
}