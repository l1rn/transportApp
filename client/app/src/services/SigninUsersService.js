import axios from "axios";

const API_BASE_URL = "http://localhost:8080"

class SigninUserService{
    signinUser(userData) {
        return axios.post(`${API_BASE_URL}/auth/sign-in`, {
                username: userData.username,
                password: userData.password
            }
        );
    }
    getRoleUser(){
        return axios.get(`${API_BASE_URL}/users/me/role`, {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json" 
            }
        })
    }
    
}
export default new SigninUserService();