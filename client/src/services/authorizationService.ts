import { UserData } from "@/types/userdata";
import { AxiosResponse } from "axios";
import { api } from "./api";
import { useLoginStore } from "@/stores/authStore";
import router from "@/routers/router";

class AuthorizationService{
    public async signInUser(data: UserData): Promise<AxiosResponse> {
        return await api.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {
            username: data.username,
            password: data.password
        })
    }

    public async logoutUser(): Promise<boolean> {
        try{
            await api.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/logout`,);
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
        return api.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-up`, {
            username: data.username,
            password: data.password
        });
    }

    getRoleUser(){
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
    }
}

export const authorizationService = new AuthorizationService();