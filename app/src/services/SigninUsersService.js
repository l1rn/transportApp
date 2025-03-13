import axios from "axios";


class SigninUserService{
    signinUser(userData) {
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-in`, {
                username: userData.username,
                password: userData.password
            }
        );
    }
    getRoleUser(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`, {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json" 
            }
        })
    }
    
}
export default new SigninUserService();