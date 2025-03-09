import axios from "axios";
import { cancelTokenRefresh, scheduleTokenRefresh } from "./api";

const BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings";
const MY_BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings/my"
const MY_BOOKING_CANCEL_API_BASE_URL = "http://localhost:8080/profile/bookings/my/cancel";
const REFRESH_API_BASE_URL = "http://localhost:8080/auth/refresh"

class BookingService{
    addBooking(routeId){
        const bookingData = {
            routeId: routeId
        };
        return axios.post(BOOKING_API_BASE_URL, bookingData, {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type": "application/json" 
            }
        });
    }
    checkRefreshToken(){
        return axios.post(REFRESH_API_BASE_URL, {
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
        return axios.get(MY_BOOKING_API_BASE_URL,
            {
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                    "Content-Type": "application/json",
                },
            }
        );
    }
    cancelMyBooking(bookingId){
            return axios.patch(MY_BOOKING_CANCEL_API_BASE_URL,  {bookingId},
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