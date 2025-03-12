import axios from "axios";

class AdminService {
    getAllUsers(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/all`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    getAllBookings(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/profile/bookings/all`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    addRoute(routeData){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/add`, routeData,
            {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
                }
            }
        )
    }
    deleteRoute(routeId){
        return axios.delete(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/delete/${routeId}`,{
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    putRoute(routeId, routeData){
        return axios.put(`${process.env.VUE_APP_BACKEND_APP_API}/routes/panel/update/${routeId}`, routeData,{
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    postSetRoleAdmin(userId){
        return axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/users/admin/${userId}`, {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
}

export default new AdminService();