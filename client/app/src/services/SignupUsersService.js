import axios from "axios";

const USERS_SIGNUP_API_BASE_URL = "http://localhost:8080/auth/sign-up"


class SignupUsersService{
    signupUser(username, password){
        const signupData = {
            username: username,
            password: password
        };
        return axios.post(USERS_SIGNUP_API_BASE_URL, signupData);
    }
}

export default new SignupUsersService();