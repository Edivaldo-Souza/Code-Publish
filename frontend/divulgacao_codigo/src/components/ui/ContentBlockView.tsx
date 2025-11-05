import { useState } from "react";
import { AttachedFile } from "@/types/publication";
import CodePreview from "../CodePreview";

const CopyIcon = () => <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg>;
const DownloadIcon = () => <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path></svg>;
const CheckIcon = () => <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>;

interface ContentBlockViewProps{
    attachedFile:AttachedFile
}

export function ContentBlockView({attachedFile}:ContentBlockViewProps){
    const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(attachedFile.previewContent);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleDownload = () => {
    const element = document.createElement("a");
    const file = new Blob([attachedFile.previewContent], {type: 'text/plain'});
    element.href = URL.createObjectURL(file);
    element.download = "codigo.txt";
    document.body.appendChild(element);
    element.click();
  };

  function getFileExtension():string{
    if(attachedFile.file){
      return attachedFile.file.name.split(".")[1]
    }
    return "txt"
  }

  return(
    <div className="border border-gray-200 rounded-lg">
      <div className="relative bg-[rgb(17,27,39)] rounded-t-lg">

          <div className="overflow-x-auto">
            <CodePreview content={attachedFile.previewContent} language={getFileExtension()}/>
          </div>
        
        <div className="absolute top-3 right-3 flex items-center gap-2">
          <button onClick={handleCopy} className="p-2 rounded-md bg-indigo-600 hover:bg-gray-600 transition-colors">
            {copied ? <CheckIcon /> : <CopyIcon />}
          </button>
          <button onClick={handleDownload} className="p-2 rounded-md bg-indigo-600 hover:bg-gray-600 transition-colors">
            <DownloadIcon />
          </button>
        </div>
      </div>
      <div className="p-6">
        <p className="text-gray-700 leading-relaxed">{attachedFile.description}</p>
      </div>
    </div>
  )
}