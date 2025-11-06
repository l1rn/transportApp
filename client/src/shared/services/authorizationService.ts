import { UserData } from "@/shared/types/userData";
import { AxiosError, AxiosResponse } from "axios";
import { api } from "./api";
import { useLoginStore } from "@/shared/stores/authStore";
import router from "@/routers/router";
import { useAuthStore } from "../stores/useLoginStore";

class AuthorizationService{
    constructor() {
        this.authStore = useAuthStore();
    }
    public async signInUser(data: UserData): Promise<AxiosResponse> {
        const response = await api.post(`/auth/sign-in`, {
            username: data.username,
            password: data.password
        });
        return response;
    }

    public async logoutUser(): Promise<boolean> {
        try{
            await api.post(`/auth/logout`,);
            router.replace('/')
            return true;
        }
        catch(err){
            console.log(err);
            return false;
        }
    }

    public async signupUser(data: UserData): Promise<AxiosResponse> {
        return api.post(`/auth/sign-up`, {
            username: data.username,
            password: data.password
        });
    }
}

export const authorizationService = new AuthorizationService();