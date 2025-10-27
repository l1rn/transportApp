import notification from "@/plugins/notifications";
import { userService } from "@/services/userService";
import { useModalStore } from "@/stores/useModalStore";
import { AxiosError, AxiosResponse } from "axios";
import { ref } from "vue";

export const useRequestHandler = () => {
    const vModelValue = ref('');
    const modalStore = useModalStore();

    const handleRequest = async(
        confirmFunc: (data: number | string) => Promise<AxiosResponse>,
        successMsg: string,
        errorMessage: string = 'Произошла ошибка',
        modalKey: string,
        modalKeyClose: string, 
        extraCheck?: boolean
    ): Promise<void> => {
        if(!vModelValue.value || !extraCheck) {
            notification.error(errorMessage);
            return;
        }

        try{
            await confirmFunc(vModelValue.value);
            notification.success(successMsg);
            vModelValue.value = "";
            if(modalKeyClose){
                modalStore.close(modalKeyClose);
                modalStore.open(modalKey)
            }
            else {
                modalStore.open(modalKey);
            }
        }
        catch(e){
            const axiosError = e as AxiosError;
            console.log(axiosError.status);
        }
    }

    const handleConfirm = async(
        confirmFunc: (code: string) => Promise<AxiosResponse>,
        successMsg: string,
        modalKey: string
    ): Promise<void> => {
        if(!vModelValue.value || vModelValue.value.length < 6){
            notification.error("Введите действительный код!");
            return;
        }

        try{
            await confirmFunc(vModelValue.value);
            notification.success(successMsg);
            vModelValue.value = '';
            modalStore.close(modalKey);
        }
        catch(e){
            const axiosError = e as AxiosError;
            console.log(axiosError);
        }
    }

    return {
        vModelValue,
        handleConfirm,
        handleRequest
    }
}
