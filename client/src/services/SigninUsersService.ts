import { UserData } from "@/types/userData";
import axios from "axios";


class SigninUserService{
    async signinUser(userData: UserData) {
        return await axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {
                username: userData.username,
                password: userData.password
        });
    }
    getRoleUser(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
    }
    
}
export default new SigninUserService();