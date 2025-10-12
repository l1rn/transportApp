import { AxiosResponse, InternalAxiosRequestConfig } from "axios";
import { api } from "./api";
import { PaginatedRoute } from "@/types/route";

class RouteService {
    public async searchRoutes(
        routeFrom?: string, 
        routeTo?: string, 
        transport?: string, 
        date?: string, 
        page: number = 0, 
        size: number = 10
    ): Promise<AxiosResponse<PaginatedRoute>> {
        return await api.get('/routes/search', { 
            params: Object.fromEntries(
                Object.entries({ routeFrom, routeTo, transport, date, page, size })
                    .filter(([_, value]) => value != null)
            ),
            headers: {}
        } as InternalAxiosRequestConfig);
    }

    public getRoutes(): Promise<AxiosResponse> {
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes`);
    }
}

export const routesService = new RouteService();