import axios from "axios";


class RoutesService{
    getRoutes(){
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes`);
    }
    searchRoutes(routeFrom: string, routeTo: string, date: string, transport: string, page = 0, size = 10){
        const params = {
            routeFrom: routeFrom || null,
            routeTo: routeTo || null,
            date: date || null,
            transport: transport || null,
            page: page,
            size: size
        };
        return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes/search`, {params});
    }
}

export default new RoutesService();