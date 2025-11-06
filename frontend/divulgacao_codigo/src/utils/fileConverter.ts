import api from "@/lib/api";

export async function fileConverter(url:string,filename:string,minType:string): Promise<File>{

    const response = await api.get(url,{transformResponse:[(data)=>data]})
    const blob = response.data
    
    const file = new File([blob], filename, {type:minType})

    return file;
}