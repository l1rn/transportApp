import notification from "@/plugins/notifications";
import { userService } from "@/services/userService";
import { useModalStore } from "@/stores/useModalStore";
import { AxiosError, AxiosResponse } from "axios";
import { Ref } from "vue";

export const useRequestHandler = () => {
    const modalStore = useModalStore();

    const handleRequest = async(
        confirmFunc: (data: number | string | null) => Promise<AxiosResponse>,
        successMsg: string,
        errorMessage: string = 'Произошла ошибка',
        modalKey: string,
        modalKeyClose: string, 
        vmodel: Ref<number | string | null>,
        extraCheck?: boolean,
    ): Promise<void> => {
        if(extraCheck){
            notification.error(errorMessage);
            return;
        }
        if(!vmodel.value) {
            notification.error(errorMessage);
            return;
        }

        try{
            await confirmFunc(vmodel.value);
            notification.success(successMsg);
            vmodel.value = "";
            modalStore.close(modalKeyClose);
            modalStore.open(modalKey);
        }
        catch(e){
            const axiosError = e as AxiosError;
            console.log(axiosError.status);
        }
    }

    const handleConfirm = async(
        confirmFunc: (code: string) => Promise<AxiosResponse>,
        successMsg: string,
        modalKey: string,
        vmodel: Ref<string | null>
    ): Promise<void> => {
        if(!vmodel.value){
            notification.error("Введите действительный код!");
            return;
        }

        try{
            await confirmFunc(vmodel.value);
            notification.success(successMsg);
            vmodel.value = '';
            modalStore.close(modalKey);
        }
        catch(e){
            const axiosError = e as AxiosError;
            console.log(axiosError);
        }
    }

    return {
        handleConfirm,
        handleRequest
    }
}
