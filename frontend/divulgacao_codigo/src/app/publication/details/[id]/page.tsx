import PublicationDetails from "@/components/PublicationDetails"

export default function PublicationView({params}:{params:{id:string}}){
    
    return(
        <PublicationDetails publicationId={params.id}/>
    )
}