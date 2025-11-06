
import React, { useState,useEffect } from "react"
import { AttachedFile,FileApi } from "@/types/publication"
import CodePreview from "./CodePreview";

interface ModalProps{
    isOpen: boolean,
    onClose: () => void,
    onSave: (data: Omit<AttachedFile, 'id'>) => void,
    editingFile?: AttachedFile
}

export default function FileModal({isOpen,onClose,onSave,editingFile}:ModalProps){
    const [description,setDescription] = useState<string>('');
    const [file,setFile] = useState<File|null>(null);
    const [fileApi,setFileApi] = useState<FileApi|null>(null)
    const [filePreview,setFilePreview] = useState<string|null>(null);
    const [error,setError] = useState<string|null>(null);
    const [isLoading,setIsLoading] = useState(false);

  useEffect(() => {
    if (isOpen && editingFile?.fileResource) {
      if (editingFile) {
        setDescription(editingFile.description);
        setFilePreview(editingFile.previewContent);
        setFile(editingFile.fileResource);
        setFileApi(editingFile.file);
      } else {
        setDescription('');
        setFilePreview(null);
        setFile(null);
      }
    }
  }, [isOpen,editingFile]);

    if(!isOpen) return null;

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = event.target.files?.[0]; 

        if(!selectedFile){
          //setFile(null);
          //setFilePreview(null);
          return;
        }

        setFile(selectedFile);
        setIsLoading(true);

        const binaryMimeTypes = [
        'image/',
        'video/',
        'audio/',
        'application/pdf',
        'application/zip',
        'application/octet-stream',
        'application/x-zip-compressed',
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document']

        const isBinary = binaryMimeTypes.some(binaryType =>
          selectedFile.type.startsWith(binaryType)
        )

        if(isBinary){
          setError(`O tipo de arquivo não pode ser aceito: ${selectedFile.type}`)
          event.target.value = '';
          return
        }

        if(editingFile){
          setFileApi(null)
        }
        
        const reader = new FileReader()
          reader.onload = (e: ProgressEvent<FileReader>) =>{
            const text= e.target?.result
            if(typeof text === "string"){
              setFilePreview(text)
            }
            else{
              setError("Não foi possível abrir o arquivo")
            }
            setIsLoading(false)
          }
          reader.onerror = () =>{
            setFilePreview("Erro ao tentar ler o arquivo");
            setIsLoading(false);
          }

          reader.readAsText(selectedFile);
    }

    const handleSave = () =>{
      if(description.trim()===""){
        setError("Defina uma descrição para o arquivo")
      }
      else if(file && filePreview){
        onSave({
          file:fileApi,
          fileResource:file,
          description:description,
          previewContent: filePreview
        })
        setDescription('');
        setFile(null);
        setFilePreview(null);
        setError(null)
      }
    }

    
    const handleCancel = () =>{
        onClose();
        setError('')
        setDescription('');
        setFile(null);
        setFilePreview(null);
    }

    function getFileExtension():string{
    if(fileApi){
      return fileApi.name.split(".")[1]
    }
    if(file){
      return file.name.split(".")[1]
    }
    return "txt"
    }

    const previewContainerClasses = `bg-gray-800 text-white rounded-md p-4 min-h-[200px] overflow-auto text-sm
    ${!filePreview || isLoading ? 'flex items-center justify-center' : ''}`

    return(
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl transform transition-all duration-300 scale-100">
        
        <label className="flex items-center justify-center bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-4 rounded-t-lg cursor-pointer transition-colors">
          <input type="file" className="hidden" onChange={handleFileChange} />
          Selecionar arquivo
        </label>

        <div className="p-6 space-y-6">
          { filePreview ? (
            <div className='bg-[rgb(17,27,39)] p-4 text-white rounded-md min-h-[200px] max-h-[200px] overflow-auto text-sm'>
              <pre className="whitespace-pre-wrap text-left w-full text-indigo-200">
                  <code>{filePreview}</code>
              </pre>
            </div>
          ) : null}
          
          <textarea
            placeholder="Descrição geral"
            rows={4}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full px-4 py-3 text-black rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 placeholder-gray-400"
          ></textarea>

          <div className="flex justify-center gap-4">
            <button
              type="button"
              onClick={handleSave}
              className="px-8 py-3 font-semibold text-white bg-indigo-600 rounded-md hover:bg-indigo-700 transition-colors"
            >
              Salvar
            </button>
            <button
              type="button"
              onClick={handleCancel}
              className="px-8 py-3 font-semibold text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-100 transition-colors"
            >
              Cancelar
            </button>
          </div>

          { error ? 
          <p className="text-red-600">
            {error}
          </p> : null
          }
        </div>
      </div>
    </div>
    )
}