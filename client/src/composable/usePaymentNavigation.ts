import { inject, provide, ref } from "vue";

type PaymentView = 'current-payment' | 'my-payments' | 'promocodes';

const PaymentNavigationSymbol = Symbol("payment-navigation");

export const usePaymentNavigationProvider = () => {
    const currentView = ref<PaymentView>("current-payment");

    const setView = (view: PaymentView) => {
        currentView.value = view;
    };

    provide(PaymentNavigationSymbol, {
        currentView,
        setView
    });

    return {
        currentView,
        setView
    };
}

export const usePaymentNavigation = () => {
    const navigation = inject(PaymentNavigationSymbol);

    if(!navigation)
        throw new Error('usePaymentNavigation must be used within a component that provides payment navigation');
    
    return navigation;
}