import axios from "axios";
import router from "@/routers/router";
axios.interceptors.request.use(config => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            
            try {
                const newAccessToken = await refreshTokenRequest(
                    localStorage.getItem("refreshToken")
                );
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return axios(originalRequest);
            } catch (refreshError) {
                handleAuthError();
                return Promise.reject(refreshError);
            }
        }
        
        return Promise.reject(error);
    }
);

async function refreshTokenRequest(refreshToken) {
    try {
        const response = await axios.post('http://localhost:8080/auth/refresh', { refreshToken });
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        scheduleTokenRefresh();
        return response.data.accessToken;
    } catch (error) {
        handleAuthError();
        throw error;
    }
}

function handleAuthError() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    if (router.currentRoute.path !== '/home') {
        router.replace('/home');
    }
}

let refreshTimeoutId = null;

export function scheduleTokenRefresh() {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) return;

    const payload = JSON.parse(atob(accessToken.split('.')[1]));
    const expiresAt = payload.exp * 1000;
    const timeout = expiresAt - Date.now() - 10000;

    if (timeout > 0) {
        refreshTimeoutId = setTimeout(() => {
            refreshTokenRequest(localStorage.getItem("refreshToken"));
        }, timeout);
    }
}

export function cancelTokenRefresh() {
    if (refreshTimeoutId) {
        clearTimeout(refreshTimeoutId);
        refreshTimeoutId = null;
    }
}