import { api } from "./api";

class RoutesService{
    getRoutes(){
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes`);
    }
    searchRoutes(routeFrom, routeTo, date, transport, page = 0, size = 10){
        const params = {
            routeFrom: routeFrom || null,
            routeTo: routeTo || null,
            date: date || null,
            transport: transport || null,
            page: page,
            size: size
        };
        Object.keys(params).forEach(key => {
            if (params[key] === null || params[key] === undefined) {
                delete params[key];
            }
        });
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes/search`, {params});
    }
}

export default new RoutesService();