export default interface ResponseApiError{
    success:boolean,
    message:string,
    error:string,
    errorCode:number,
    timestamp:number,
    path:string
}