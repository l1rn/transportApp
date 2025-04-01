import axios from "axios";


class LogoutService {
    logoutUser() {
        try{
            axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/logout`,);
            return true;
        }
        catch(err){
            console.log(err);
            return false;
        }
    }
}

export default new LogoutService();