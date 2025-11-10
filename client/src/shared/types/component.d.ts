import { Ref } from "vue";

export type ActionType = "add" | "edit" | "delete"

export interface ModalPropsView {
    icon: string;
    title: string;
    desc: string;
    inputPlaceholder: string;
    buttonName: string;
    inputType: string;
    submitFunc: () => void | Promise<void>;
    model: Ref<string | number | null>;
    storeKey: string;
    externalId?: string;
}

export interface PaymentPageProps {
    title: string;
    price: number;
    paymentMethods: Array<string>;
    hasEmail: boolean;
} 