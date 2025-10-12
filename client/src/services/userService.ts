import { AxiosResponse } from "axios";
import { api } from "./api";

class UserService {
    public async getUserInfo(): Promise<AxiosResponse> {
        return await api.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me`)
    }
}

export const userService = new UserService(); 