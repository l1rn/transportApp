import axios from "axios";

const ROUTES_API_BASE_URL = 'http://localhost:8080/routes';

class RoutesService{
    getRoutes(){
        return axios.get(ROUTES_API_BASE_URL);
    }
    searchRoutes(routeFrom, routeTo, date, transport){
        const params = {
            routeFrom: routeFrom || null,
            routeTo: routeTo || null,
            date: date || null,
            transport: transport || null,
        };
        Object.keys(params).forEach(key => {
            if (params[key] === null || params[key] === undefined) {
                delete params[key];
            }
        });

        return axios.get(`http://localhost:8080/routes/search?`, params);
    }
}

export default new RoutesService();