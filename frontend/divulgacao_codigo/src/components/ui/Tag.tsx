
interface TagProps{
    name:string
}

export function Tag({name}:TagProps){
    return(
    <span className="bg-gray-200 text-gray-800 text-sm font-medium px-4 py-1.5 rounded-full">
      {name}
    </span>
    )
}