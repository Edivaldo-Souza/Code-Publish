"use client"

import api from "@/lib/api";
import { SingleSelectOption, MetaData } from "@/types/selectables";
import { useId,useEffect, useState } from "react";
import toast from "react-hot-toast";
import Select, {SingleValue } from "react-select";

interface SelectSingleInputPros{
    value: SingleSelectOption | null,
    onChange: (selectedOptions:SingleValue<SingleSelectOption>) => void
    url:string
}

export default function SelectSingleInput({value,onChange,url}:SelectSingleInputPros){
    const [options,setOptions] = useState<SingleSelectOption[]>([])
    const [isLoading,setIsLoading] = useState(true)
    const [typeMetaData,setTypeMetaData] = useState<string>('')
    const stableId = useId() 

    useEffect(()=>{
        if(url.substring(3)==='categories'){
            setTypeMetaData('Categoria')
        }
        else setTypeMetaData('Linguagem de programação')

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
        <div>
        <Select
            isClearable
            instanceId={stableId}
            options={options}
            isLoading={isLoading}
            value={value}
            onChange={onChange}
            required
            placeholder={`Selecione a ${typeMetaData}`}
            noOptionsMessage={()=>(`Nenhuma ${typeMetaData} encontrada`)}
            loadingMessage={()=>(`Carregando ${typeMetaData}...`)}
            styles={{
                menu: (base) => ({ ...base, zIndex: 9999 })
            }}
        />
        </div>
    )
}