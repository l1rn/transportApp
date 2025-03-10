import axios from "axios";

const ADMIN_ALL_USERS_BASE_API_URL = "http://localhost:8080/users/all"
const ADMIN_ALL_BOOKINGS_BASE_API_URL = "http://localhost:8080/profile/bookings/all"
const ADMIN_POST_ADD_ROUTE_API_URL = "http://localhost:8080/routes/panel/add"
const ADMIN_DELETE_ROUTE_API_URL = "http://localhost:8080/routes/panel/delete"
const ADMIN_PUT_ROUTE_API_URL = "http://localhost:8080/routes/panel/update"
class AdminService {
    getAllUsers(){
        return axios.get(ADMIN_ALL_USERS_BASE_API_URL, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    getAllBookings(){
        return axios.get(ADMIN_ALL_BOOKINGS_BASE_API_URL, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    addRoute(routeData){
        return axios.post(ADMIN_POST_ADD_ROUTE_API_URL, routeData,
            {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
                }
            }
        )
    }
    deleteRoute(routeId){
        return axios.delete(`${ADMIN_DELETE_ROUTE_API_URL}/${routeId}`,{
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
    putRoute(routeId, routeData){
        return axios.put(`${ADMIN_PUT_ROUTE_API_URL}/${routeId}`, routeData,{
            headers:{
                "Authorization": `Bearer ${localStorage.getItem('accessToken')}`,
                "Content-Type": "application/json"
            }
        })
    }
}

export default new AdminService();