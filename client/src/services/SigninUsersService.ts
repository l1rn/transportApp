import axios from "axios";
import { UserData } from "@/types/userdata"

class SigninUserService{
    async signInUser(data: UserData) {
        return await axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {data})
    }
    getRoleUser(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
    }
    
}
export default new SigninUserService();