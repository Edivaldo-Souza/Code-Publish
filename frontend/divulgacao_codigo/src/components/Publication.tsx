'use client';

import { useState,useEffect,FormEvent } from "react";
import api from "@/lib/api";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import FileModal from "@/components/FileModal";
import ContentBlockCard from "@/components/ContentBlockCard";
import { AttachedFile,FileApi } from "@/types/publication";
import { fileConverter } from "@/utils/fileConverter";
import {SelectOption, SingleSelectOption } from "@/types/selectables";
import { MultiValue, SingleValue } from "react-select";
import SelectMultiInput from "./SelectMultiInput";
import SelectSingleInput from "./SelectSingleInput";
import getPublicationById from "@/utils/getPublication";
import axios from "axios";

interface PublicationProps{
  editingPublicationId?:string
}

export default function PublicationForm({editingPublicationId}:PublicationProps){

  const isUpdateMode = !!editingPublicationId;

  const [isModalOpen,setIsModalOpen] = useState<boolean>(false);
  const [title, setTitle] = useState<string>('');
  const [description,setDescription] = useState<string>('');
  const [selectedTags,setSelectTags] = useState<MultiValue<SelectOption>>()
  const [selectedCategory,setSelectedCategory] = useState<SingleSelectOption|null>(null)
  const [selectedProgramingLanguage,setSelectedProgramingLanguage] = useState<SingleSelectOption|null>(null)
  const [attachedFiles, setAttachedFiles] = useState<AttachedFile[]>([]);
  const [editingFileId,setEditingFileId] = useState<number|null>(null);
  const [isLoadingFiles,setIsLoadingFiles] = useState(isUpdateMode);
  const router = useRouter();

 useEffect(() => {
    if (isUpdateMode) {
      
      const fetchFiles = async () => {
        const editingPublication = await getPublicationById(editingPublicationId);

        setTitle(editingPublication.title)
        setSelectedCategory({
          value:editingPublication.category.id,
          label:editingPublication.category.name
        })
        setSelectedProgramingLanguage({
          value:editingPublication.programingLanguage.id,
          label:editingPublication.programingLanguage.name
        })
        setDescription(editingPublication.description)
        setSelectTags(editingPublication.tags ? editingPublication.tags.map(tag=>{
          return{
            label:tag.name,
            value:tag.name
          }
        }):[])

        const filePromises = editingPublication.components.map(async (component) => {
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
        setIsLoadingFiles(false);
      };

      fetchFiles();
    }
  }, [editingPublicationId, isUpdateMode]);

  const handleRemoveFile = (id: number) => {
    setAttachedFiles(prevFiles => prevFiles.filter(file => file.id !== id));
  };

  const handleBackButton = () =>{
    router.back()
  }

  const handleStartEditFile = (id: number) => {
    setEditingFileId(id);
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    let filesMetadata
    const formData = new FormData();

    if(isUpdateMode){
      filesMetadata = attachedFiles.map((item,index) =>{
        if(!item.file){
          return{
            id:null,
            file:null,
            fileId:`file_0${index}`,
            description:item.description
          } 
        }
        else{
          return{
            id:item.id,
            file: item.file,
            fileId:null,
            description:item.description
          }
        }
      })

      attachedFiles.forEach((item, index) => {
      if(!item.file && item.fileResource) formData.append(`file_0${index}`, item.fileResource);
      })

    }
    else{
      filesMetadata = attachedFiles.map((item,index)=>({
          id:null,
          file:null,
          fileId:`file_0${index}`,
          description:item.description
      }))

      attachedFiles.forEach((item, index) => {
      if(item.fileResource) formData.append(`file_0${index}`, item.fileResource);
      })

    }

    const tags = selectedTags ? selectedTags.map(tag=>({name:tag.label})) : [];

    const publication = {
          title:title,
          programingLanguageId:selectedProgramingLanguage?.value,
          categoryId:selectedCategory?.value,
          description:description,
          components:filesMetadata,
          tags:tags
      }

    console.log("publication_to_be_sent",publication)

    formData.append('data', JSON.stringify(publication));

    console.log('Dados a serem enviados para a API:');
      for (const [key, value] of formData.entries()) {
        if(value instanceof File){
          console.log(`${key}`,{name:value.name, size:value.size})
        }
        console.log(`${key}:`, value);
    }

    try{
        let response;
        if(isUpdateMode){
          response = await api.put(`v1/publications/${editingPublicationId}`,formData);
        }
        else{
          response = await api.post("v1/publications",formData)
        }

        if(response.status!==201 && response.status!==200){
            toast.error(response.data.error)
        }
        else{
            toast.success("Dados salvos com sucesso!")
            router.back();
        }
    } catch(error){
      if(axios.isAxiosError(error)){
        toast.error(`${error.response?.data.error}`)
      }
    }
  };

  const handleOpenModal = () =>{
    setEditingFileId(null);
    setIsModalOpen(true);
  }

  const handleCloseModal = () =>{
    setEditingFileId(null);
    setIsModalOpen(false);
  }

  const handleSaveModal = (description:string,fileResource:File,file:FileApi,previewContent:string) =>{
    setAttachedFiles(prev => [...prev,{description,fileResource,file,previewContent,id:Date.now()}]);
    handleCloseModal();
  }

  const handleSaveModalData = (data: Omit<AttachedFile,'id'>) => {
    if(editingFileId){
      setAttachedFiles(prevFiles =>
        prevFiles.map(file =>
          file.id === editingFileId ? {...file,...data} : file
        )
      );
    }
    else{
      setAttachedFiles(prev => [...prev,{...data,id:Date.now()}]);
    }
    handleCloseModal()
  }

  const handleCategoryChange = (newValue: SingleValue<SingleSelectOption>) => {
    setSelectedCategory(newValue)
  }

  const handleProgramingLanguageChange = (newValue: SingleValue<SingleSelectOption>) =>{
    setSelectedProgramingLanguage(newValue)
  }

  const fileToEdit = editingFileId ? attachedFiles.find(f => f.id === editingFileId) : undefined;

    return (
    <main className="bg-slate-100 flex items-center justify-center min-h-screen p-4">
      <div className="bg-white p-8 sm:p-12 rounded-xl shadow-lg w-full max-w-4xl">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">
          Nova Publicação
        </h1>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-6">
            <div className="text-black grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <input type="text" placeholder="Título" id="title" value={title} onChange={(e) => setTitle(e.target.value)} required className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition" />
              </div>
                <SelectSingleInput
                value={selectedProgramingLanguage}
                onChange={handleProgramingLanguageChange}
                url='v1/programing-languages'
                />
              <div>
                <SelectMultiInput 
                value={selectedTags as SelectOption[]}
                onChange={setSelectTags}/>
              </div>
              <div>
                <SelectSingleInput
                value={selectedCategory}
                onChange={handleCategoryChange}
                url="v1/categories"
                />
              </div>
            </div>

            <textarea 
              placeholder="Descrição geral" 
              rows={5}
              className=" text-black w-full px-4 py-3 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 placeholder-gray-400"
              value={description} onChange={(e) => setDescription(e.target.value)} required
            ></textarea>

            
            {attachedFiles.length === 0 ? (
            <label className="flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors"
              onClick={handleOpenModal}>
              <div className="flex flex-col items-center justify-center pt-5 pb-6">
                <div className="flex items-center justify-center w-16 h-16 mb-4 rounded-full bg-indigo-100">
                  <svg className="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path></svg>
                </div>
                <p className="mb-2 text-md font-semibold text-gray-600">Adicionar novo arquivo</p>
              </div>
            </label>) : null}

            <div className="flex items-start gap-6">
              <div className="w-full space-y-6">
                {attachedFiles.map(attachedFile => (
                  <ContentBlockCard 
                    key={attachedFile.id}
                    attachedFile={attachedFile}
                    onDelete={() => handleRemoveFile(attachedFile.id)}
                    onEdit={() => handleStartEditFile(attachedFile.id)}
                  />
                ))}
              </div>
            </div>

            {attachedFiles.length > 0 ? (
              <label className="flex flex-col items-center justify-center w-full h-20 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors"
              onClick={handleOpenModal}>
              <div className="flex flex-col items-center justify-center pt-5 pb-6">
                <svg className="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path></svg>
                <p className="mb-2 text-md font-semibold text-gray-600">Adicionar novo arquivo</p>
              </div>
            </label>
            ) : null}

          </div>
          <div className="mt-8 flex justify-center gap-4">
            <button type="submit" className="px-6 py-3 bg-indigo-600 text-white font-bold rounded-lg hover:bg-indigo-700 transition-colors shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">Salvar</button>
            <button type="button" onClick={handleBackButton} className="px-6 py-3 font-bold rounded text-indigo-600 bg-transparent border rounded-md shadow-sm hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"> Voltar</button>
          </div>
        </form>
        
      </div>
      <FileModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSave={handleSaveModalData}
        editingFile = {fileToEdit}
      > 
      </FileModal>
    </main>
  );
}