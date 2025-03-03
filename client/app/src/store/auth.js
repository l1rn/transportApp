import axios from "axios";

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        if(error.response.status === 401 && !originalRequest._retry){
            originalRequest._retry = true;
            try{
                const newAccessToken = await refreshToken();
                sessionStorage.setItem('accessToken', newAccessToken);
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return axios(originalRequest);
            }
            catch(refreshError){
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    });

const refreshToken = async () => {
    const response = await axios.post(
            "http://localhost:8080/auth/refresh",
            {},
            { withCredentials: true }
        );
    return response.data.accessToken;
};