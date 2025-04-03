import axios from "axios";

class UserService{
    getUserAgent(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me`)
    }
}
export default new UserService()