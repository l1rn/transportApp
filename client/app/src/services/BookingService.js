import axios from "axios";
import { cancelTokenRefresh, scheduleTokenRefresh } from "./api";

class BookingService{
    addBooking(routeId){
        const bookingData = {
            routeId: routeId
        };
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings`, bookingData, {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type": "application/json" 
            }
        });
    }
    checkRefreshToken(){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/refresh`, {
            refreshToken: localStorage.getItem('refreshToken')
            }
        ).then(response => {
            localStorage.setItem('refreshToken', response.data.refreshToken);
            localStorage.setItem('accessToken', response.data.accessToken);
            cancelTokenRefresh();
            scheduleTokenRefresh();
            return response;
        }
    )
    }
    getMyBooking(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/my`,
            {
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                    "Content-Type": "application/json",
                },
            }
        );
    }
    cancelMyBooking(bookingId){
            return axios.patch(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/my/cancel`,  {bookingId},
                {
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                        "Content-Type": "application/json",
                    }
                }
            );

    }
}

export default new BookingService();