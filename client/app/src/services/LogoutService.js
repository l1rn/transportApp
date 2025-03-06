import axios from "axios";

const LOGOUT_API_URL = 'http://localhost:8080/auth/logout';

class LogoutService {
    logoutUser(accessToken, refreshToken) {
        try{
            axios.post(LOGOUT_API_URL, {
                    refreshToken,
                },
                {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': "application/json",
                    }
                },
            );
            return true;
        }
        catch(err){
            console.log(err);
            return false;
        }
    }
}

export default new LogoutService();