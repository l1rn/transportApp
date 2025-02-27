import axios from "axios";

const USERS_SIGNIN_API_BASE_URL = "http://localhost:8080/auth/sign-in"

class SigninUserService{
    signinUser(username, password) {
    return axios.post(USERS_SIGNIN_API_BASE_URL, { 
        username, 
        password 
    });
}
    
}

export default new SigninUserService();