import axios from "axios";

const USERS_SIGNIN_API_BASE_URL = "http://localhost:8080/auth/sign-in"

class SigninUserService{
    signinUser(userData) {
        return axios.post(USERS_SIGNIN_API_BASE_URL, {
                username: userData.username,
                password: userData.password
            }
        );
    }
}
export default new SigninUserService();