import axios, { AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from "axios";
import router from "@/routers/router";

import { createPinia, setActivePinia } from "pinia";
import { useAuthStore } from "../stores/useLoginStore";

setActivePinia(createPinia());

interface CustomAxiosRequestConfig extends InternalAxiosRequestConfig {
    _retry?: boolean;
    _retryCount?: number;
}

export const CONFIG = {
    MAX_RETRY_ATTEMPTS: 3,
    REFRESH_BUFFER_MS: 1000 * 50,
    BASE_URL: `${import.meta.env.VUE_APP_BACKEND_URL}`,
    TIMEOUT: 10000
};

interface AuthResponse extends AxiosResponse {
    headers: {
        'x-token-expires'?: string;
    };
}

interface RefreshTokenResponse{
    accessToken?: string;
    expiresIn?: number;
}

class ApiInterceptor {
    private api: AxiosInstance;
    private refreshTimeoutId: ReturnType<typeof setTimeout> | null = null;
    private isRefreshing = false;
    private pendingRequests: Array<() => void> = [];
    private authStore;
    private lastRefreshTime: number = 0;

    private readonly MIN_REFRESH_INTERVAL = 30000;

    constructor() {
        this.authStore = useAuthStore();
        this.api = axios.create({
            baseURL: CONFIG.BASE_URL,
            timeout: CONFIG.TIMEOUT,
            withCredentials: true,
        });

        this.setupInterceptors();
    }

    private setupInterceptors(): void {
        this.api.interceptors.request.use(
            (config: InternalAxiosRequestConfig) => {
                return config;
            },
            (error: AxiosError) => {
                return Promise.reject(error);
            }
        );
        this.api.interceptors.response.use(
            (response: AxiosResponse) => {
                if(response.config.url?.includes("/auth")){
                    this.handleAuthResponse(response as AuthResponse);
                }

                if(response.config.url?.includes('/auth') && response.status >= 200 && response.status < 300){
                    this.handleAuthResponse(response as AuthResponse);
                }
                
                const customConfig = response.config as CustomAxiosRequestConfig;
                if(customConfig._retryCount){
                    delete customConfig._retryCount;
                }

                return response;
            },
            async (error: AxiosError) => {
                return this.handleResponseError(error);
            }
        )
    }

    private handleAuthResponse(response: AuthResponse): void {
        const expiresHeader = response.headers['x-token-expires'];
        if(expiresHeader){
            const expiresIn = parseInt(expiresHeader, 10);
            if(!isNaN(expiresIn) && expiresIn > 0){
                this.scheduleTokenRefresh(expiresIn);
            } else {
                console.warn('⚠️ Invalid expiresIn value:', expiresIn);
            }
        } 
    }

    private handleResponseError(err: AxiosError): Promise<AxiosResponse> {
        const originalRequest = err.config as CustomAxiosRequestConfig;

        if(!originalRequest || originalRequest.url?.includes('/auth') || !err.response){
            return Promise.reject(err)
        }

        if(err.response.status === 401 || err.response.status === 403){
            return this.handleUnauthorizedError(originalRequest);
        }

        if(err.response.status >= 500){
            console.error("Server status: ", err.response.status);
        }

        return Promise.reject(err);
    }


    private async handleUnauthorizedError(originalRequest: CustomAxiosRequestConfig): Promise<AxiosResponse> {
        const currentRoute = router.currentRoute.value;
        if(currentRoute.meta.requiresAuth === false){
            console.warn("Authentication failed but skipping redirect due to route meta");
            return Promise.reject("Unauthorized User!");
        } 
        originalRequest._retryCount = originalRequest._retryCount || 0;

        if(originalRequest._retryCount >= CONFIG.MAX_RETRY_ATTEMPTS){
            await this.handleAuthError("Max retry attempts exceeded");
            return Promise.reject(new Error("Max retry attempts exceeded"));
        }

        if(this.isRefreshing){
            return new Promise((resolve, reject) => {
                this.pendingRequests.push(() => {
                    originalRequest._retryCount!++;
                    this.api(originalRequest)
                        .then(resolve)
                        .catch(reject)
                })
            })
        }

        originalRequest._retry = true;
        originalRequest._retryCount!++;
        
        try{
            this.isRefreshing = true;
            await this.refreshTokenRequest();
            const response = await this.api(originalRequest);
            this.processPendingRequests();
            return response;
        }
        catch(refreshError){
            await this.processPendingRequests(true);
            await this.handleAuthError('Token refresh failed!');
            return Promise.reject(refreshError);
        }
        finally{
            this.isRefreshing = false;
        }
    }

    private processPendingRequests(shouldReject = false): void {
        const requests = [...this.pendingRequests];
        this.pendingRequests = [];

        requests.forEach(request => {
            if (shouldReject) {
                request();
            } else {
                request();
            }
        });
    }

    private async refreshTokenRequest(): Promise<void> {
        try{
            const response = await this.api.post<RefreshTokenResponse>(`${CONFIG.BASE_URL}/auth/refresh`);
            if(response.data.expiresIn){
                this.scheduleTokenRefresh(response.data.expiresIn);
            }
        }
        catch(error){
            localStorage.setItem('logined', 'false');
            throw error;
        }
    }

    private scheduleTokenRefresh(exp: number): void {
        this.cancelTokenRefresh();
        
        const refreshTime = exp - CONFIG.REFRESH_BUFFER_MS;
        if(refreshTime <= 0){ 
            console.warn('Token expiration time too short, refreshing immediately');
            this.refreshTokenRequest().catch((error) => 
                this.handleAuthError("Scheduled refresh failed: " + error)
            );
            return;
        }

        this.refreshTimeoutId = setTimeout(async() => {
            try {
                await this.refreshTokenRequest();
            }
            catch(error){
                console.error(error);
                await this.handleAuthError("Scheduled refresh failed");
            }
        }, refreshTime);

        console.log('✅ Token refresh scheduled in', refreshTime, 'ms');
    }

    private async handleAuthError(reason: string): Promise<void>{  
        const currentRoute = router.currentRoute.value;
        if(currentRoute.meta.requiresAuth === false){
            console.warn("Authentication failed but skipping redirect due to route meta:", reason);
            return;
        } 
        console.error("Authentication failed: ", reason);

        this.cancelTokenRefresh();
        this.isRefreshing = false;
        this.pendingRequests = [];

        try{
            await this.api.post("/auth/logout");
        }
        catch(logoutError){
            console.error("Logout request failed: ", logoutError);
        }

        this.authStore.logout();
        localStorage.setItem('logined', 'false');

        if(router.currentRoute.value.path !== '/home'){
            await router.replace('/home');
            window.location.reload();
        }
    }

    private cancelTokenRefresh(): void {
        if(this.refreshTimeoutId){
            clearTimeout(this.refreshTimeoutId);
            this.refreshTimeoutId = null;
        }
    }

    public post<T>(url: string, data?: unknown, config?: InternalAxiosRequestConfig): 
    Promise<AxiosResponse<T>> {
            return this.api.post(url, data, config);
        }

    public get<T>(url: string , config?: InternalAxiosRequestConfig):
        Promise<AxiosResponse<T>> {
            return this.api.get(url, config);
        }

    public put<T>(url: string, data?: unknown, config?: InternalAxiosRequestConfig): 
    Promise<AxiosResponse<T>> {
            return this.api.put(url, data, config);
        }

    public patch<T>(url: string, data?: unknown, config?: InternalAxiosRequestConfig): 
    Promise<AxiosResponse<T>> {
            return this.api.patch(url, data, config);
        }

    public delete<T>(url: string, data?: unknown, config?: InternalAxiosRequestConfig): 
    Promise<AxiosResponse<T>> {
            return this.api.delete(url, config);
        }

    public getInstance(): AxiosInstance{
        return this.api;
    }
}

export const api = new ApiInterceptor();