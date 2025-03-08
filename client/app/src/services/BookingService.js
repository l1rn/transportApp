import axios from "axios";

const BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings";
const MY_BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings/my"
const MY_BOOKING_CANCEL_API_BASE_URL = "http://localhost:8080/profile/bookings/my/cancel";
class BookingService{
    addBooking(routeId, config){
        const bookingData = {
            routeId: routeId
        };
        return axios.post(BOOKING_API_BASE_URL, bookingData, config);
    }
    getMyBooking(){
        return axios.get(MY_BOOKING_API_BASE_URL,
            {
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                    "Content-Type": "application/json",
                },
            });
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