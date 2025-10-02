'use client';

import { useState,ChangeEvent,FormEvent } from "react";
import Link from "next/link";
import api from "@/lib/api";
import { useRouter } from "next/navigation";

// Define a estrutura de um arquivo anexado, que inclui o objeto File e sua descrição
interface AttachedFile {
  file: File;
  description: string;
}


enum Category {
  DESENVOLVIMENTO_WEB = 'Desenvolvimento para Web',
  INATIVO = 'Inativo',
  PENDENTE = 'Pendente',
}

export default function Publication(){
    // Estado para o campo de texto principal da entidade
  const [title, setTitle] = useState<string>('');
  const [category, setCategory] = useState<Category>(Category.DESENVOLVIMENTO_WEB);
  const [programingLanguage, setProgramingLanguage] = useState<string>('');
  const [description,setDescription] = useState<string>('');
  
  // Estado para armazenar a lista de arquivos com suas descrições
  const [attachedFiles, setAttachedFiles] = useState<AttachedFile[]>([]);
  const router = useRouter();

  /**
   * Manipula a seleção de novos arquivos.
   * Converte o FileList em um array e adiciona os novos arquivos ao estado.
   */
  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const newFiles = Array.from(e.target.files).map(file => ({
        file,
        description: '', // Inicia com uma descrição vazia
      }));
      setAttachedFiles(prevFiles => [...prevFiles, ...newFiles]);
    }
  };

  /**
   * Atualiza a descrição de um arquivo específico na lista.
   * @param index O índice do arquivo na lista a ser atualizado.
   * @param description A nova descrição para o arquivo.
   */
  const handleFileDescriptionChange = (index: number, description: string) => {
    setAttachedFiles(prevFiles =>
      prevFiles.map((item, i) =>
        i === index ? { ...item, description } : item
      )
    );
  };

  /**
   * Remove um arquivo da lista com base no seu índice.
   * @param index O índice do arquivo a ser removido.
   */
  const handleRemoveFile = (index: number) => {
    setAttachedFiles(prevFiles => prevFiles.filter((_, i) => i !== index));
  };

  /**
   * Manipula o envio do formulário.
   * Prepara os dados em um objeto FormData para envio.
   */
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const filesMetada = attachedFiles.map((item,index)=>({
        id:null,
        file:null,
        fileId:`file_0${index}`,
        description:item.description
    }))

    const tags = [{
      name:'Tag'
    }]

    const publication = {
        title:title,
        programingLanguage:programingLanguage,
        category:category,
        description:description,
        components:filesMetada,
        tags:tags
    }

    // FormData é ideal para enviar arquivos e dados de texto juntos
    const formData = new FormData();
    formData.append('data', JSON.stringify(publication));
    //formData.append('entityData',title);


    // Anexa cada arquivo e sua respectiva descrição
    attachedFiles.forEach((item, index) => {
     formData.append(`file_0${index}`, item.file);
    })

    console.log('Dados a serem enviados para a API:');
    // Para inspecionar o FormData, você pode iterar sole
    for (let [key, value] of formData.entries()) {
      if(value instanceof File){
        console.log(`${key}`,{name:value.name, size:value.size})
      }
      console.log(`${key}:`, value);
    }
    
    alert('Formulário pronto para envio! Verifique o console para ver o objeto FormData.');

    try{
        const response = await api.post("v1/publications",formData);

        if(response.status!==201){
            console.log(response.data.error)
        }
        else{
            router.push('/home');
        }
    } catch(error:any){
        console.log(error)
    }
  };

    return (
    <main className="bg-gray-50 min-h-screen p-4 sm:p-8">
      <div className="max-w-4xl mx-auto bg-white p-6 sm:p-8 rounded-xl shadow-lg">
        <h1 className="text-3xl font-bold text-gray-800 text-center mb-8">
          Nova Publicação
        </h1>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="text-gray-500 grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <input type="text" placeholder="Título" id="title" value={title} onChange={(e) => setTitle(e.target.value)} required className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition" />
            </div>
            <div>
              <input type="text" placeholder="Linguagem de programação" id="programingLanguage" value={programingLanguage} onChange={(e) => setProgramingLanguage(e.target.value)} required className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition" />
            </div>
            <div>
              <input type="text" placeholder="Descrição" id="description" value={description} onChange={(e) => setDescription(e.target.value)} required className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition" />
            </div>
            <div>
              <select id="category" value={category} onChange={(e) => setCategory(e.target.value as Category)} className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition bg-white">
                {Object.values(Category).map((categoryValue) => (<option key={categoryValue} value={categoryValue}>{categoryValue}</option>))}
              </select>
            </div>
          </div>
          <div className="border-t border-gray-200 pt-6">
            <label htmlFor="files" className="block mb-2 font-semibold text-gray-700">Adicionar Arquivos</label>
            <input type="file" id="files" multiple onChange={handleFileChange} className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100" />
          </div>
          {attachedFiles.length > 0 && (
            <div className="space-y-4 pt-6">
              <h2 className="text-xl font-semibold text-gray-700">Arquivos Selecionados</h2>
              {attachedFiles.map((item, index) => (
                <div key={index} className="p-4 border border-gray-200 rounded-lg bg-gray-50 space-y-3">
                  <div className="flex items-center justify-between flex-wrap gap-2">
                      <p className="font-medium text-gray-800 break-all">{item.file.name}</p>
                      <span className="text-sm text-gray-500 bg-gray-200 px-2 py-1 rounded-full">{(item.file.size / 1024).toFixed(2)} KB</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <input type="text" placeholder="Descrição do arquivo..." value={item.description} onChange={(e) => handleFileDescriptionChange(index, e.target.value)} required className="flex-grow px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 transition" />
                    <button type="button" onClick={() => handleRemoveFile(index)} className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition-colors font-semibold">Remover</button>
                  </div>
                </div>
              ))}
            </div>
          )}
          <div className="pt-4">
            <button type="submit" className="px-6 py-3 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 transition-colors shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">Salvar</button>
            <Link href={'/home'} className="px-6 py-3 font-bold rounded text-indigo-600 bg-transparent border rounded-md shadow-sm hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"> Voltar</Link>
          </div>
        </form>
        
      </div>
    </main>
  );
}