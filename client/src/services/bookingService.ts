import { AxiosResponse } from "axios";
import { api } from "./api";

class BookingService {
    public async getMyBooking(): Promise<AxiosResponse> {
        return await api.get(`/profile/bookings/my`);
    }
    public async createBooking(routeId: number): Promise<AxiosResponse> {
        return await api.post(`/profile/bookings`, { routeId: routeId});
    }
    public async cancelBooking(bookingId: number): Promise<AxiosResponse> {
        return api.patch(`/profile/bookings/my/cancel`, { bookingId });
  }
}

export const bookingService = new BookingService();