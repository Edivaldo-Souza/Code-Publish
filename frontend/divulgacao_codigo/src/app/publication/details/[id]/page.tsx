import PublicationDetails from "@/components/PublicationDetails"

interface PublicationViewProps{
    params: Promise<{id:string}>
    searchParams: Promise<{redirect_to?:string}>
}

export default async function PublicationView({params,searchParams}:PublicationViewProps){
    const {id} = await params
    const {redirect_to} = await searchParams
    return(
        <PublicationDetails publicationId={id} redirect_to={redirect_to}/>
    )
}