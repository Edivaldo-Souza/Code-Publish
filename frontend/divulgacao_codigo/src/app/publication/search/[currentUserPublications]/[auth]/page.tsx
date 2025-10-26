import SearchPublications from "@/components/SearchPublications";

interface PublicationSearchProps{
  params: Promise<{
    currentUserPublications:string
    auth:string
  }>
}

export default async function PublicationSearch({params}:PublicationSearchProps){
  const {currentUserPublications,auth} = await params;
  return (
    <SearchPublications currentUserPublications={currentUserPublications} isLoggedIn={auth}/>
  )
}