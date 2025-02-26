import axios from "axios";

const ROUTES_API_BASE_URL = 'http://localhost:8080/routes';

class RoutesService{
    getRoutes(){
        return axios.get(ROUTES_API_BASE_URL);
    }
}

export default new RoutesService();