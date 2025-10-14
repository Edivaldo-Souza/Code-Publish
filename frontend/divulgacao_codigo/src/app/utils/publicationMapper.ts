import { Publication,PublicationApi } from "@/types/publication";

export function mapApiResponseToPublication(apiData: PublicationApi): Publication{
    const publicationForForm: Publication ={
        id:apiData.id,
        title: apiData.title,
        programingLanguage: apiData.programingLanguage,
        category: apiData.category,
        tags: apiData.tags,
        description: apiData.description,
        upvotesAmount: apiData.upvotesAmount,
        downvotesAmount: apiData.downvotesAmount,
        components: apiData.components.map(component => {
            return{
                id:component.id,
                description:component.description,
                file:{
                    id:component.file.id,
                    name:component.file.name,
                    type:component.file.type,
                    url:component.file.url
                },
                previewContent:''
            }
        })
    }

    return publicationForForm;
}