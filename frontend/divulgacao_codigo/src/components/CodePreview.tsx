"use client";

import {Prism as SyntaxHighlighter} from "react-syntax-highlighter"
import {coldarkDark} from "react-syntax-highlighter/dist/esm/styles/prism"

interface CodePreviewProps{
    content: string,
    language: string 
}

const CodePreview: React.FC<CodePreviewProps> = ({content,language}) =>{
    return(
        <SyntaxHighlighter
            language={language}
            style={coldarkDark}
            showLineNumbers={true}
        >
            {String(content).trim()}
        </SyntaxHighlighter>
    )
}

export default CodePreview