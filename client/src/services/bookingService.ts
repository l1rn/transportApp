import { AxiosResponse } from "axios";
import { api } from "./api";

class BookingService {
    public async getMyBooking(): Promise<AxiosResponse> {
        return await api.get(`/profile/bookings/my`);
    }
}

export const bookingService = new BookingService();