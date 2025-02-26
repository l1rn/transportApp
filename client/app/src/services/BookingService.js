import axios from "axios";

const BOOKING_API_BASE_URL = "http://localhost:8080/profile/bookings";

class BookingService{
    addBooking(routeId){
        const bookingData = {
            routeId: routeId
        };
        return axios.post(BOOKING_API_BASE_URL, bookingData);
    }
}

export default new BookingService();