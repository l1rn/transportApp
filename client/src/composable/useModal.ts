import { inject, InjectionKey, ref } from "vue";

export interface ModalOptions {
    component: any;
    props?: Record<string, any>;
    onClose?: () => void;
    onConfirm?: (data?: any) => void;
}

export interface ModalInstance {
    id: string;
    component: any;
    props?: Record<string, any>;
    isOpen: boolean;
}

export const ModalKey: InjectionKey<ReturnType<typeof useModal>> = Symbol('modal');

export const useModal = () => {
    const modals = ref<Map<string, ModalInstance>>(new Map());
    const modalStack = ref<string[]>([]);

    const openModal = (id: string, options: ModalOptions) => {
        const modalInstance: ModalInstance = {
            id, 
            component: options.component,
            props: options.props,
            isOpen: true
        };

        modals.value.set(id, modalInstance);
        modalStack.value.push(id);
    };

    const closeModal = (id: string) => {
        const modal = modals.value.get(id);
        if(modal) {
            modal.isOpen = false;
            setTimeout(() => {
                modals.value.delete(id);
                modalStack.value = modalStack.value.filter(modalId => modalId !== id)
            }, 300);
        }
    };

    const closeAll = () => {
        modals.value.clear();
        modalStack.value = [];
    }

    const getCurrentModal = () => {
        if(modalStack.value.length) return null;
        return modals.value.get(modalStack.value[modalStack.value.length - 1]);
    };

    return {
        modals: modals as Readonly<typeof modals>,
        modalStack: modalStack as Readonly<typeof modalStack>,
        openModal,
        closeModal,
        closeAll,
        getCurrentModal
    }
}

export const useInjectModal = () => {
    const modal = inject(ModalKey);
    if(!modal)
        throw new Error("useInjectModal must be used within ModalProvider")
    return modal;
}