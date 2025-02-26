import axios from "axios";

const USERS_SIGNIN_API_BASE_URL = "http://localhost:8080/auth/sign-in"

class SigninUserService{
    signinUser(username, password){
        const signinData = {
            username: username,
            password: password
        }
        return axios.post(USERS_SIGNIN_API_BASE_URL, signinData);
    }
    
}

export default new SigninUserService();