import router from "@/routers/router";
import axios from "axios";
import { useLoginStore } from "@/stores/authStore";

class LogoutService {
    logoutUser() {
        try{
            axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/logout`,);
            const loginStore = useLoginStore();
            router.replace('/')
            loginStore.logout()
            return true;
        }
        catch(err){
            console.log(err);
            return false;
        }
    }
}

export default new LogoutService();