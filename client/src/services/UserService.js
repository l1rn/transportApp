import axios from "axios";

class UserService{
    getUserAgent(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me`)
    }
    changeUserPassword(passwordRequest){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/change/password`, passwordRequest)
    }
}
export default new UserService()