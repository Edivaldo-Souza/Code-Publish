import api from "@/lib/api";
import { Tag,SelectOption } from "@/types/selectables";
import { useId,useEffect, useState } from "react";
import toast from "react-hot-toast";
import Select, { MultiValue } from "react-select";

interface SelectMultiInputProps{
    value: SelectOption[],
    onChange: (selectedOptions:MultiValue<SelectOption>) => void
}

export default function SelectMultiInput({value,onChange}:SelectMultiInputProps){
    const [options,setOptions] = useState<SelectOption[]>([])
    const [isLoading,setIsLoading] = useState(true)
    const stableId = useId()

    useEffect(()=>{
        const fetchOptions = async () =>{
            try{
                const response = await api.get<Tag>("v1/tags")
                const formatedOptions = response.data.data.map(option => ({
                    value:option.name,
                    label:option.name
                }))
                setOptions(formatedOptions)
            } catch(error){
                toast.error("Não foi possível buscar as tags")
            } finally{
                setIsLoading(false)
            }
        }
        fetchOptions();
    },[])

    return (
        <Select
            isMulti
            instanceId={stableId}
            options={options}
            isLoading={isLoading}
            value={value}
            onChange={onChange}
            required
            placeholder='Selecione as tags'
            noOptionsMessage={()=>("Nenhuma tag encontrada")}
            loadingMessage={()=>("Carregando tags...")}
            styles={{
                menu: (base) => ({ ...base, zIndex: 9999 })
            }}
        />
    )
}