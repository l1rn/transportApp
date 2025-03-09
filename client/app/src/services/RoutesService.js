import axios from "axios";

const ROUTES_API_BASE_URL = 'http://localhost:8080/routes';

class RoutesService{
    getRoutes(){
        return axios.get(ROUTES_API_BASE_URL);
    }
    searchRoutes(routeFrom, routeTo, date, transport, page = 0, size = 10){
        const params = {};
        if(routeFrom) params.routeFrom = routeFrom;
        if(routeTo) params.routeTo = routeTo;
        if(date) params.date = date;
        if(transport) params.transport = transport;
        params.page = page;
        params.size = size;
        return axios.get(ROUTES_API_BASE_URL, {params});
    }
    searchByDate(date){
        return axios.get(ROUTES_API_BASE_URL, {params: date});
    }
    searchByTransport(transport){
        return axios.get(ROUTES_API_BASE_URL, {params: transport});
    }
}

export default new RoutesService();