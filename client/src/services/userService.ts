import { AxiosResponse } from "axios";
import { api } from "./api";
import { ChangePasswordRequest, UserInfo } from "@/types/userData";

class UserService {
    // user basic
    public async getUserInfo(): Promise<AxiosResponse<UserInfo>> {
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

    // user account
    public async requestUserEmail(newEmail: string): Promise<AxiosResponse> {
        return await api.post(`/users/set-email`, { email: newEmail });
    }
    
    public async confirmUserEmail(code: string): Promise<AxiosResponse> {
        return await api.post(`/users/confirm-email`, { code: code });
    }
}

export const userService = new UserService(); 