import axios from "axios";
import { UserData } from "@/types/userdata"

class signInUserService{
    async signInUser(data: UserData) {
        return await axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {
            username: data.username,
            password: data.password
        })
    }
    getRoleUser(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
    }
    
}
export default new signInUserService();