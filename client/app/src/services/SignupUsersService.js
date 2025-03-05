import axios from "axios";

const USERS_SIGNUP_API_BASE_URL = "http://localhost:8080/auth/sign-up"


class SignupUsersService{
    signupUser(userData) {
        return axios.post(USERS_SIGNUP_API_BASE_URL, {
            username: userData.username,
            password: userData.password
        });
    }
}

export default new SignupUsersService();