import { MetaDataDto, Tag,TagDto } from "./selectables";

export interface PublicationApi{
  id:number;
  title:string;
  description:string;
  category:MetaDataDto;
  programingLanguage:MetaDataDto;
  tags:TagDto[];
  components:PublicationComponentApi[],
  upvotesAmount:number;
  downvotesAmount:number;
  authorName:string;
}


export interface PublicationComponentApi{
    id:number;
    file:FileApi;
    fileId:string;
    description:string;
}

export interface FileApi{
  id: number;
  name: string;
  type: string;
  url: string;
}

export interface Publication{
  id:number;
  title:string;
  description:string;
  category:MetaDataDto;
  programingLanguage:MetaDataDto;
  tags:TagDto[];
  components:AttachedFile[];
  upvotesAmount:number;
  downvotesAmount:number;
  authorName:string;
}

export interface AttachedFile {
  id:number;
  fileResource?: File;
  file: FileApi | null;
  previewContent: string;
  description: string;
}