import api from "@/lib/api";
import { useRouter } from "next/navigation";


export default function Logout(){
    const router = useRouter();

    const handleLogout = async () => {
        try {
        
            await api.post('/v1/auth/logout');
            
            router.push('/login');
        
        } catch (error) {
            console.error('Falha ao fazer logout', error);
        }
    };

    return(<button className="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                onClick={handleLogout}>Sair</button>);
}