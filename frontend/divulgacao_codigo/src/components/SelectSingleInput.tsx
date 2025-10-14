import api from "@/lib/api";
import { SingleSelectOption, MetaData } from "@/types/selectables";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import Select, { MultiValue, SingleValue } from "react-select";

interface SelectSingleInputPros{
    value: SingleSelectOption | null,
    onChange: (selectedOptions:SingleValue<SingleSelectOption>) => void
    url:string
}

export default function SelectSingleInput({value,onChange,url}:SelectSingleInputPros){
    const [options,setOptions] = useState<SingleSelectOption[]>([])
    const [isLoading,setIsLoading] = useState(true)
    const [typeMetaData,setTypeMetaData] = useState<string>('')

    useEffect(()=>{
        if(url.substring(3)==='categories'){
            setTypeMetaData('Categorias')
        }
        else setTypeMetaData('Linguagens de programação')

        const fetchOptions = async () =>{
            try{
                const response = await api.get<MetaData>(url)
                const formatedOptions = response.data.data.map(option => ({
                    value:option.id,
                    label:option.name
                }))
                setOptions(formatedOptions)
            } catch(error){
                 toast.error(`Não foi possível buscar as ${typeMetaData}`)
            } finally{
                setIsLoading(false)
            }
        }
        fetchOptions();
    },[])

    return (
        <Select
            isClearable
            options={options}
            isLoading={isLoading}
            value={value}
            onChange={onChange}
            placeholder={`Selecione as ${typeMetaData}`}
            noOptionsMessage={()=>(`Nenhuma ${typeMetaData} encontrada`)}
            loadingMessage={()=>(`Carregando ${typeMetaData}...`)}
            styles={{
                menu: (base) => ({ ...base, zIndex: 9999 })
            }}
        />
    )
}