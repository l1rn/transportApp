import { AxiosResponse, InternalAxiosRequestConfig } from "axios";
import { api } from "./api";
import { PaginatedRoute, Route, RouteFilter } from "@/shared/types/route";
import { PaginatedResponse } from "../types/response";

class RouteService {
    public async searchRoutes(
        routeFilter: RouteFilter,
        page: number = 0, 
        size: number = 10
    ): Promise<AxiosResponse<PaginatedResponse<Route>>> {
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

    public async findCitiesFrom(q: string): Promise<AxiosResponse> {
        return await api.get(`/routes/s/cities-from?q=${q}`)
    }

    public async findCitiesTo(q: string): Promise<AxiosResponse> {
        return await api.get(`/routes/s/cities-to?q=${q}`)
    }
    
    // admin
    public async updateRoute(routeId: number, routeData: Route): Promise<AxiosResponse> {
        return await api.put(`/routes/panel/update/${routeId}`, routeData);
    }
}

export const routesService = new RouteService();