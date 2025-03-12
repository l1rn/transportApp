import axios from "axios";


class LogoutService {
    logoutUser(accessToken, refreshToken) {
        try{
            axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/logout`, {
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