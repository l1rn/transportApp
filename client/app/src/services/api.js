import axios from "axios";

axios.interceptors.request.use(config => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
    },
    (error) => Promise.reject(error)
);

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        if (error.response?.status === 401 && !originalRequest._retry) {
            try {
                originalRequest._retry = true;
                const newAccessToken = await refreshTokenRequest(
                    localStorage.getItem("accessToken"),
                );
                originalRequest.headers.Authorization = `Bearer ${ newAccessToken }`;
                return axios(originalRequest);
            } catch (refreshError) {
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                this.$router.replace('/home');
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

async function refreshTokenRequest(refreshToken) {
    try {
        const response = await axios.post('http://localhost:8080/auth/refresh', {
            refreshToken: refreshToken
        });
        const { accessToken, refreshToken: newRefreshToken } = response.data;

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        scheduleTokenRefresh();

    } catch (error) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.reload();
    }
}

let refreshTimeoutId = null;

export function scheduleTokenRefresh (){
    const refreshToken = localStorage.getItem("refreshToken");

    const accessToken = localStorage.getItem("accessToken");
    const payload = JSON.parse(atob(accessToken.split('.')[1]));
    const expiresAt = payload.exp * 1000;
    const now = Date.now();

    const timeout = expiresAt - now - 5000;

    if(refreshToken){
        cancelTokenRefresh();
    }

    if(timeout > 0){
        refreshTimeoutId = setTimeout(() =>{
            refreshTokenRequest(refreshToken);
        }, timeout);
    }
}
export function cancelTokenRefresh (){
    if(refreshTimeoutId){
        clearTimeout(refreshTimeoutId);
        refreshTimeoutId = null;
    }
}
