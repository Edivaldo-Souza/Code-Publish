import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios'
import { error } from 'console'
import toast from 'react-hot-toast'

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    withCredentials: true
})

api.interceptors.request.use(
    (config) =>{
        return config
    },
    (error)=>{
        return Promise.reject(error)
    }
)

api.interceptors.response.use(
    (response) => {
        return response
    },

    (error:AxiosError) =>{
        const originalRequest = error.config as InternalAxiosRequestConfig
        const url = originalRequest.url
        if(error.response?.status === 403){
            if(url && url.includes("rating")){
                return Promise.reject(error)
            }
            else{
            toast.error("A sessão do usuário foi expirada")
                if(typeof window !== undefined){
                    window.location.href = "/publication/search/false/false"
                }
            }
        }
        return Promise.reject(error)
    }
)

/*api.interceptors.request.use(
    (config) =>{
        if( typeof window !== undefined){
            const token = localStorage.getItem('authToken')
            if(token){
                config.headers.Authorization = `Bearer ${token}`
            }
        }
        return config;
    },
    (error)=>{
        return Promise.reject(error)
    }
)*/

export default api;