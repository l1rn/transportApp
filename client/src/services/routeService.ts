import { AxiosResponse, InternalAxiosRequestConfig } from "axios";
import { api } from "./api";
import { PaginatedRoute, RouteFilter } from "@/types/route";

class RouteService {
    public async searchRoutes(
        routeFilter: RouteFilter,
        page: number = 0, 
        size: number = 10
    ): Promise<AxiosResponse<PaginatedRoute>> {
        const searchParams = new URLSearchParams();
        
        Object.entries(routeFilter).forEach(([key, value]) => {
            if(value != null && value !== ''){
                searchParams.append(key, value.toString());
            }
        });

        searchParams.append('page', page.toString());
        searchParams.append('size', size.toString());

        return await api.get('/routes/search', { 
            params: searchParams,
            headers: {}
        } as InternalAxiosRequestConfig);
    }

    public getRoutes(): Promise<AxiosResponse> {
        return api.get(`${process.env.VUE_APP_BACKEND_APP_API}/routes`);
    }
}

export const routesService = new RouteService();