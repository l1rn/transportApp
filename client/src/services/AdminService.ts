import axios from "axios";

class AdminService {
    getAllUsers(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/all`)
    }
    getAllBookings(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/all`)
    }
    addRoute(routeData){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/add`, routeData)
    }
    deleteRoute(routeId: number){
        return axios.delete(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/delete/${routeId}`)
    }
    putRoute(routeId: number, routeData){
        return axios.put(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/update/${routeId}`, routeData)
    }
    postSetRoleAdmin(userId: number){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/users/admin/${userId}`)
    }
    deleteUser(userId: number){
        return axios.delete(`${process.env.VUE_APP_BACKEND_APP_API}/users/admin/delete/${userId}`)
    }
    patchBooking(bookingId: number){
        return axios.patch(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/cancel/${bookingId}`)
    }
}

export default new AdminService();