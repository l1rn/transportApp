import { api } from "./api"

class AdminService {
    getAllUsers(){
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/all`)
    }
    getAllBookings(){
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/all`)
    }
    addRoute(routeData: any){
        return api.post(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/add`, routeData)
    }
    deleteRoute(routeId: number){
        return api.delete(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/delete/${routeId}`)
    }
    putRoute(routeId: number, routeData: any){
        return api.put(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/update/${routeId}`, routeData)
    }
    postSetRoleAdmin(userId: number){
        return api.post(`${process.env.VUE_APP_BACKEND_APP_API}/users/admin/${userId}`)
    }
    deleteUser(userId: number){
        return api.delete(`${process.env.VUE_APP_BACKEND_APP_API}/users/admin/delete/${userId}`)
    }
    patchBooking(bookingId: number){
        return api.patch(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/cancel/${bookingId}`)
    }
}

export const adminService = new AdminService();