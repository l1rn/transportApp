import { UserData } from "@/types/userData";
import { AxiosResponse } from "axios";
import { api } from "./api";
import { useLoginStore } from "@/stores/authStore";
import router from "@/routers/router";

class AuthorizationService{
    public async signInUser(data: UserData): Promise<AxiosResponse> {
        return await api.post(`/auth/sign-in`, {
            username: data.username,
            password: data.password
        })
    }

    public async logoutUser(): Promise<boolean> {
        try{
            await api.post(`/auth/logout`,);
            const loginStore = useLoginStore();
            router.replace('/')
            loginStore.logout()
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

    getRoleUser(){
        return api.get(`/users/me/role`)
    }
}

export const authorizationService = new AuthorizationService();