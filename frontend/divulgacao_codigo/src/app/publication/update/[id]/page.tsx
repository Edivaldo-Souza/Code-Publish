
import PublicationForm from "@/components/Publication";

interface PublicationEditProps{
    params:Promise<{id:string}>
}

export default async function PublicationEdit({params}:PublicationEditProps){
    const {id} = await params

    return (
        <PublicationForm editingPublicationId={id}/>
    )
}