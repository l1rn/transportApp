import { api } from "./api"

class AdminService {
    getAllUsers(){
        return api.get(`/users/all`)
    }
    getAllBookings(){
        return api.get(`/profile/bookings/all`)
    }
    addRoute(routeData: any){
        return api.post(`/routes/panel/add`, routeData)
    }
    deleteRoute(routeId: number){
        return api.delete(`/routes/panel/delete/${routeId}`)
    }
    putRoute(routeId: number, routeData: any){
        return api.put(`/routes/panel/update/${routeId}`, routeData)
    }
    postSetRoleAdmin(userId: number){
        return api.post(`/users/admin/${userId}`)
    }
    deleteUser(userId: number){
        return api.delete(`/users/admin/delete/${userId}`)
    }
    patchBooking(bookingId: number){
        return api.patch(`/profile/bookings/cancel/${bookingId}`)
    }
}

export const adminService = new AdminService();