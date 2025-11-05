"use client";

import { useEffect,useState } from "react"
import { AttachedFile, Publication } from "@/types/publication"
import getPublicationById from "@/utils/getPublication";
import VotingComponent from "./ui/Voting";
import { Tag } from "./ui/Tag";
import { ContentBlockView } from "./ui/ContentBlockView";
import { fileConverter } from "@/utils/fileConverter";
import { useRouter,usePathname,useSearchParams } from "next/navigation";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { FaX } from "react-icons/fa6";
import Link from 'next/link';
import axios from "axios";

interface PublicationDetailsProps{
    publicationId:string
    redirect_to?:string
}

export default function PublicationDetails({publicationId,redirect_to}:PublicationDetailsProps){
    const [publication,setPublication] = useState<Publication>();
    const [attachedFiles,setAttachedFiles] = useState<AttachedFile[]>([]);
    const [isLoading,setIsLoading] = useState(true);
    const router = useRouter();
    const pathname = usePathname()
    const searchParams = useSearchParams()

    const handleLogin = () =>{
      const callbackUrl = encodeURIComponent(pathname)
      router.push(`/login?redirect_to=${callbackUrl}`)
    }

    const handleBackButton = () =>{
      const redirect = searchParams.get("redirect_to")
      
      if(redirect?.includes("login")){
        router.push("/publication/search/false/true")
      }
      else if(redirect?.includes("lastPage")){
        router.push(redirect.split("lastPage")[0].concat("lastPage=1"))
      }
      else router.back()
    }

    useEffect(()=>{
        const fetchPublication = async() =>{
            const searchedPublication = await getPublicationById(publicationId)
            
            const username = localStorage.getItem("code_publish_username")
            if(username){
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
            
            setAttachedFiles(attachedFiles);
            }
            setPublication(searchedPublication)
            setIsLoading(false)
        }

        fetchPublication()
        console.log(publication)
    },[publicationId])

    const updatePublication = (addVote:number,removeVote:number,isPositive:boolean) => {
      if(publication){
        if(isPositive){
          setPublication({
            id:publication.id,
            authorName:publication.authorName,
            category:publication.category,
            components:publication.components,
            description:publication.description,
            downvotesAmount:publication.downvotesAmount-removeVote,
            programingLanguage:publication.programingLanguage,
            title:publication.title,
            tags:publication.tags,
            upvotesAmount:publication.upvotesAmount+addVote
          })
        }
        else{
          setPublication({
            id:publication.id,
            authorName:publication.authorName,
            category:publication.category,
            components:publication.components,
            description:publication.description,
            downvotesAmount:publication.downvotesAmount+addVote,
            programingLanguage:publication.programingLanguage,
            title:publication.title,
            tags:publication.tags,
            upvotesAmount:publication.upvotesAmount-removeVote
          })
        }
      }
    }

    const handleVoting = async (isPositive:boolean) =>{
      try{
        const response = await api.patch(`v1/publications/rating/${publication?.id}?positive=${isPositive}`)
        if(response.status===200 && publication){
          if(isPositive){
            if(response.data.data.updateVotes){
              updatePublication(1,1,isPositive)
            }
            else{
              updatePublication(1,0,isPositive)
            }
          } else{
            if(response.data.data.updateVotes){
              updatePublication(1,1,isPositive)
            }
            else{
              updatePublication(1,0,isPositive)
            }
          }
        }
      } catch(error){
        if(axios.isAxiosError(error)){
          if(error.status===403){
            toast.error("É preciso estar logado para realizar essa ação")
          }
          else toast.error(`${error.response?.data.error}`)
        }
      }
    }
    
    return(
        <main className="bg-slate-100 flex items-center justify-center min-h-screen p-4">
            <div className="w-full max-w-4xl">
            <header className="flex justify-between items-start mb-6">
                <h1 className="text-4xl sm:text-5xl font-bold text-gray-800">
                    {publication?.title}
                </h1>
                <VotingComponent rate={handleVoting} upvotes={publication?.upvotesAmount} downvotes={publication?.downvotesAmount} />
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

        {attachedFiles.length > 0 ?
          <section className="space-y-6">
          {attachedFiles.map(file => (
            <ContentBlockView key={file.id} attachedFile={file} />
          ))}
          </section>
        : 
          <div className="bg-gray-50 border border-gray-200 rounded-lg p-8 sm:p-12 flex flex-col items-center justify-center text-center">
            <FaX className="text-red-500 w-24 h-24 stroke-[20]" />

            <h3 className="mt-6 text-xl sm:text-2xl font-semibold text-gray-800 max-w-md">
              Realize o login na plataforma para ter acesso aos arquivos da
              publicação
            </h3>
            <button
              onClick={handleLogin}
              className="mt-8 px-6 py-3 bg-indigo-600 text-white text-lg font-medium rounded-lg shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 transition duration-150 ease-in-out"
            >
              Entrar
            </button>
          </div>
        }
        
        <footer className="mt-12 text-center">
          <button 
            type="button"
            onClick={handleBackButton}
            className="px-6 py-3 font-bold rounded text-indigo-600 bg-transparent border rounded-md shadow-sm hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Voltar
          </button>
        </footer>
        </div>
        </main>
    )
}