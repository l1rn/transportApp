import { Ref } from "vue";

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
}