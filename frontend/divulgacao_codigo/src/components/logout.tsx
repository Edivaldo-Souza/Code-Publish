"use client"

import api from "@/lib/api";
import axios from "axios";
import { useRouter } from "next/navigation";
import toast from "react-hot-toast";


export default function Logout(){
    const router = useRouter();

    const handleLogout = async () => {
        try {
        
            await api.post('/v1/auth/logout');

            localStorage.removeItem("code_publish_username")
            router.push('/');
        
        } catch (error) {
            if(axios.isAxiosError(error)){
                toast.error(`Erro ao realizar o login ${error.response?.data.error}`) 
            }   
        }
    };

    return(<button className="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                onClick={handleLogout}>Sair</button>);
}