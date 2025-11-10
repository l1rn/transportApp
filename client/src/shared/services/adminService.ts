import { AxiosResponse } from "axios"
import { api } from "./api"
import { Route } from "../types/route";

class AdminService {
    public async getAllUsers(): Promise<AxiosResponse> {
        return await api.get(`/users/all`)
    }
    public async getAllBookings(page: number = 0, size: number = 10): Promise<AxiosResponse> {
        return await api.get(`/profile/bookings/all?page=${page}&size=${size}`);
    }
    public async addRoute(routeData: Route): Promise<AxiosResponse> {
        return await api.post(`/routes/panel/add`, routeData);
    }
    public async deleteRoute(routeId: number): Promise<AxiosResponse> {
        return await api.delete(`/routes/panel/delete/${routeId}`);
    }
    public async putRoute(routeId: number, routeData: any): Promise<AxiosResponse> {
        return await api.put(`/routes/panel/update/${routeId}`, routeData);
    }
    public async postSetRoleAdmin(userId: number): Promise<AxiosResponse> {
        return await api.post(`/users/admin/${userId}`);
    }
    public async deleteUser(userId: number): Promise<AxiosResponse> {
        return await api.delete(`/users/admin/delete/${userId}`)
    }
    public async patchBooking(bookingId: number): Promise<AxiosResponse> {
        return await api.patch(`/profile/bookings/cancel/${bookingId}`)
    }
}

export const adminService = new AdminService();