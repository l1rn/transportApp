import axios from "axios";
import router from "@/routers/router";

axios.defaults.withCredentials = true;

let retryAttempts = 0;
let tokenExpiration;

axios.interceptors.response.use(
    response => {
        if(response.config.url.includes('/auth')){
            const expiresHeader = response.headers['x-token-expires'];
            if (expiresHeader) {
                tokenExpiration = Date.now() + parseInt(expiresHeader, 10);
                scheduleTokenRefresh();
            }
        }
        return response;
    },
    async error => {
        const originalRequest = error.config;
        if (error.response?.status === 401 && !originalRequest._retry && retryAttempts < 5) {
            originalRequest._retry = true;
            retryAttempts = retryAttempts + 1;
            try {
                await refreshTokenRequest();
                return axios(originalRequest);
            } catch (refreshError) {
                handleAuthError();
                return Promise.reject(refreshError);
            }
        } 
        else{
            retryAttempts = 0;
        }
        
        return Promise.reject(error);
    }
);

async function refreshTokenRequest() {
    try {
        const response = await axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/refresh`);
        return response.data;
    } catch (error) {
        handleAuthError();
        throw error;
    }
}

function handleAuthError() {
    cancelTokenRefresh();
    axios.post(`${process.env.VUE_APP_BACKEND_APP_API}/auth/logout`);
    router.replace('/home').then(() => {
        window.location.reload(); 
    });
}

let refreshTimeoutId = null;
export function getRoleFromToken(){
    return axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/users/me/role`)
        .then(response => response.data.role)
        .catch(() => null)
}
export function scheduleTokenRefresh() {
    cancelTokenRefresh();
    if (!tokenExpiration) {
        console.warn('No token expiration time available');
        return;
      }
    
      const timeout = tokenExpiration - Date.now() - 5000; 
      
      if (timeout > 0) {
        refreshTimeoutId = setTimeout(async () => {
          try {
            await refreshTokenRequest();
            scheduleTokenRefresh(); 
          } catch (error) {
            handleAuthError();
          }
        }, timeout);
      } else {
        refreshTokenRequest(); 
      }
}

export function cancelTokenRefresh() {
    if (refreshTimeoutId) {
        clearTimeout(refreshTimeoutId);
        refreshTimeoutId = null;
    }
}

