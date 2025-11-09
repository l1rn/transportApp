import { inject, provide, ref } from "vue";

type PaymentView = 'current-payment' | 'my-payments' | 'promocodes';

const PaymentNavigationSymbol = Symbol("payment-navigation");

export const usePaymentNavigationProvider = () => {
    const currentView = ref<PaymentView>("current-payment");
    const isCurrentPaymentAvailable = ref(true);

    const setView = (view: PaymentView) => {
        currentView.value = view;
    };

    const checkAvailability = (paid: boolean) => {
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
        checkAvailability
    });

    return {
        isCurrentPaymentAvailable,
        currentView,
        setView,
        checkAvailability
    };
}

export const usePaymentNavigation = () => {
    const navigation = inject(PaymentNavigationSymbol);

    if(!navigation)
        throw new Error('usePaymentNavigation must be used within a component that provides payment navigation');
    
    return navigation;
}