import axios from "axios";

class SignupUsersService{
    signupUser(userData) {
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/sign-up`, {
            username: userData.username,
            password: userData.password
        });
    }
}

export default new SignupUsersService();