export interface TagDto{
  name:string
}

export interface MetaDataDto{
  id:number,
  name:string
}

export interface MetaData{
  data:MetaDataDto[]
}

export interface Tag{
  data:TagDto[]
}

export interface SelectOption{
    value:string,
    label:string,
}

export interface SingleSelectOption{
  value:number,
  label:string
}