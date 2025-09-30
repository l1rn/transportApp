import axios from "axios";
import { scheduleTokenRefresh } from "./api";

class BookingService {
  addBooking(routeId) {
    const bookingData = {
      routeId: routeId,
    };
    return axios.post(
      `${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings`,
      bookingData
    );
  }
  checkRefreshToken() {
    scheduleTokenRefresh();
    localStorage.setItem('logined', 'true')
    return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/refresh`);
  }
  getMyBooking() {
    return axios.get(
      `${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/my`
    );
  }
  cancelMyBooking(bookingId) {
    return axios.patch(
      `${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/my/cancel`,
      { bookingId }
    );
  }
}

export default new BookingService();
