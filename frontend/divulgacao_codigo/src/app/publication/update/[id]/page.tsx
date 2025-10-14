
import PublicationForm from "@/components/Publication";

export default async function PublicationEdit({params}:{params:{id:string}}){
    
    return (
        <PublicationForm editingPublicationId={params.id}/>
    )
}