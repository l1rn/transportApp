import { AxiosResponse } from "axios";
import { api } from "./api";

class BookingService {
    public async getMyBooking(): Promise<AxiosResponse> {
        return await api.get(`/profile/bookings/my`);
    }
    public async createBooking(routeId: number): Promise<AxiosResponse> {
        return await api.post(`/profile/bookings`, { routeId: routeId});
    }
}

export const bookingService = new BookingService();