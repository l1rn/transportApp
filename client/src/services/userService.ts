import { AxiosResponse } from "axios";
import { api } from "./api";
import { ChangePasswordRequest } from "@/types/userData";

class UserService {
    public async getUserInfo(): Promise<AxiosResponse> {
        return await api.get(`/users/me`);
    }
    
    public async changeUserPassword(r: ChangePasswordRequest): Promise<AxiosResponse> {
        return await api.patch(`/auth/change/password`, { 
            oldPassword: r.oldPassword,
            newPassword: r.newPassword
        })
    }

    public async deleteSession(id: number): Promise<AxiosResponse>{
        return await api.delete(`/auth/session/delete/${id}`);
    }
}

export const userService = new UserService(); 