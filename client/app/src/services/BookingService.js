import axios from "axios";

const BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings";
const MY_BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings/my"

class BookingService{
    addBooking(routeId, config){
        const bookingData = {
            routeId: routeId
        };
        return axios.post(BOOKING_API_BASE_URL, bookingData, config);
    }
    getMyBooking(config){
        return axios.get(MY_BOOKING_API_BASE_URL, config);
    }
}

export default new BookingService();