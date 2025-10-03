import axios from "axios";


class SigninUserService{
    async signinUser(name, password) {
        return await axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {
                username: name,
                password: password
        });
    }
    getRoleUser(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
    }
    
}
export default new SigninUserService();