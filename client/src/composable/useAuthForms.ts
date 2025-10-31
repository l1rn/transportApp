import { useModalStore } from "@/shared/stores/useModalStore"
import { computed } from "vue";

export const useAuthForms = () => {
    const modalStore = useModalStore();

    const currentForm = computed(() => {
        if(modalStore.isOpen('register')) return 'register';
        if(modalStore.isOpen('login')) return 'login';
        return null;
    });

    const switchForms = () => {
        if(currentForm.value === 'login') {
            modalStore.close('login');
            modalStore.open('register');
        }
        else if(currentForm.value === 'register'){
            modalStore.close('register');
            modalStore.open('login');
        }
    };

    const openForm = (form: 'login' | 'register') => {
        modalStore.close('login');
        modalStore.close('register');
        modalStore.open(form);
    };

    return {
        currentForm,
        switchForms,
        openForm,
        isAuthFormOpen: computed(() => currentForm.value !== null)
    }
}