import { inject, InjectionKey, provide, ref } from "vue";

type PaymentView = 'current-payment' | 'my-payments' | 'promocodes';

interface PaymentNavigation {
    isCurrentPaymentAvailable: { value: boolean };
    currentView: { value: PaymentView };
    setView: (view: PaymentView) => void;
    checkPaymentStatus: (paid: boolean) => void
}

const PaymentNavigationSymbol: InjectionKey<PaymentNavigation> = Symbol("payment-navigation");

export const usePaymentNavigationProvider = () => {
    const currentView = ref<PaymentView>("current-payment");
    const isCurrentPaymentAvailable = ref(true);

    const setView = (view: PaymentView) => {
        currentView.value = view;
    };

    const checkPaymentStatus = (paid: boolean) => {
        if(paid === true) {
            isCurrentPaymentAvailable.value = false;
        }
        else {
            isCurrentPaymentAvailable.value = true;
        }
    }

    provide(PaymentNavigationSymbol, {
        isCurrentPaymentAvailable,
        currentView,
        setView,
        checkPaymentStatus
    });

    return {
        isCurrentPaymentAvailable,
        currentView,
        setView,
        checkPaymentStatus
    };
}

export const usePaymentNavigation = () => {
    const navigation = inject(PaymentNavigationSymbol);

    if(!navigation)
        throw new Error('usePaymentNavigation must be used within a component that provides payment navigation');
    
    return navigation;
}