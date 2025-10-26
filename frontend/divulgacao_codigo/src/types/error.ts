export default interface ResponseApiError{
    error:{
        response:{
            data:{
                success:boolean,
                message:string,
                error:string,
                errorCode:number,
                timestamp:number,
                path:string
            }
        }
    }
}