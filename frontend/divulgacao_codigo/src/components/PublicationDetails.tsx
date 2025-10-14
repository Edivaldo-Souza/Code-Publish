"use client";

import { useEffect,useState } from "react"
import { AttachedFile, Publication } from "@/types/publication"
import getPublicationById from "@/utils/getPublication";
import VotingComponent from "./ui/Voting";
import { Tag } from "./ui/Tag";
import ContentBlockCard from "./ContentBlockCard";
import { ContentBlockView } from "./ui/ContentBlockView";
import { fileConverter } from "@/utils/fileConverter";
import { useRouter } from "next/navigation";

interface PublicationDetailsProps{
    publicationId:string
}

export default function PublicationDetails({publicationId}:PublicationDetailsProps){
    const [publication,setPublication] = useState<Publication>();
    const [attachedFiles,setAttachedFiles] = useState<AttachedFile[]>([]);
    const [isLoading,setIsLoading] = useState(true);
    const router = useRouter();

    useEffect(()=>{
        const fetchPublication = async() =>{
            const searchedPublication = await getPublicationById(publicationId)
        
            const filePromises = searchedPublication.components.map(async (component) => {
            try {
                if(component.file?.url){
                    const fileResource = await fileConverter(
                    component.file?.url,
                    component.file?.name,
                    component.file?.type
                    );
                    const previewContent = await fileResource.text();
                    return { ...component, fileResource, previewContent};
                }
                return component;
            } catch (error) {
                console.error(`Falha ao baixar o arquivo para o bloco ${component.id}:`, error);
                return component;
            }
            });
        
            const attachedFiles = await Promise.all(filePromises);
            
            setPublication(searchedPublication)
            setAttachedFiles(attachedFiles);
            setIsLoading(false)
        }

        fetchPublication()
        console.log(publication)
    },[publicationId])
    
    return(
        <main className="bg-slate-100 flex items-center justify-center min-h-screen p-4">
            <div className="w-full max-w-4xl">
            <header className="flex justify-between items-start mb-6">
                <h1 className="text-4xl sm:text-5xl font-bold text-gray-800">
                    {publication?.title}
                </h1>
                <VotingComponent upvotes={publication?.upvotesAmount} downvotes={publication?.downvotesAmount} />
            </header>

            <section className="space-y-4 mb-8">
          <p className="text-xl text-gray-600">
            Linguagem de programação: <span className="font-semibold text-gray-800">{publication?.programingLanguage.name}</span>
          </p>
          <div className="flex items-center gap-4 flex-wrap">
            <p className="text-xl text-gray-600">Tags:</p>
            <div className="flex items-center gap-2 flex-wrap">
              {publication?.tags.map((tag, index) => (
                <Tag key={index} name={tag.name} />
              ))}
            </div>
          </div>
          <p className="text-xl text-gray-600">
            Categoria: <span className="font-semibold text-gray-800">{publication?.category.name}</span>
          </p>
          <p className="text-lg text-gray-700 mt-2">
            Descrição: {publication?.description}
          </p>
        </section>

        <section className="space-y-6">
          {attachedFiles.map(file => (
            <ContentBlockView key={file.id} attachedFile={file} />
          ))}
        </section>

        {/* Botão de Voltar */}
        <footer className="mt-12 text-center">
          <button 
            type="button"
            onClick={()=>router.back()}
            className="px-6 py-3 font-bold rounded text-indigo-600 bg-transparent border rounded-md shadow-sm hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Voltar
          </button>
        </footer>
        </div>
        </main>
    )
}