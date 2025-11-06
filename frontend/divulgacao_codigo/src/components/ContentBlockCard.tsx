import CodePreview from "./CodePreview";
import { AttachedFile } from "@/types/publication";

interface ContenBlockCardProps{
    attachedFile:AttachedFile,
    onEdit: () => void
    onDelete: () => void
}

export default function ContentBlockCard({attachedFile,onEdit,onDelete}:ContenBlockCardProps){
  
  function getFileExtension():string{
    if(attachedFile.file){
      return attachedFile.file.name.split(".")[1]
    }
    if(attachedFile.fileResource){
      return attachedFile.fileResource.name.split(".")[1]
    }
    return "txt"
  }
  
  return(
    <div className="relative border border-gray-200 rounded-lg p-6 space-y-4">
          <div className="flex justify-end w-full">
            <button type="button"
            onClick={onDelete} 
            className="top-4 right-4 p-1.5 rounded-full text-red-500 hover:bg-red-100 transition-colors"
            >
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"><path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd"></path></svg>
            </button>
          </div>

      <div className="bg-[rgb(17,27,39)] p-4 rounded-md overflow-hidden">
          <pre className="text-indigo-200 text-sm whitespace-pre-wrap">
              <code>{attachedFile.previewContent}</code>
            </pre>
      </div>

      <p className="text-black text-sm leading-relaxed">{attachedFile.description}</p>

      <button
        type="button" 
        onClick={onEdit}
        className="px-8 py-2 font-semibold text-white bg-indigo-600 rounded-md hover:bg-indigo-700 transition-colors"
      >
        Editar
      </button>
    </div>
    )
}