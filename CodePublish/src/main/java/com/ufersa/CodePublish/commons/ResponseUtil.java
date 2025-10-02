package com.ufersa.CodePublish.commons;

public class ResponseUtil {
    public static <T> ApiResponse<T> success(T data, String message, String path){
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setMessage(message);
        response.setError(null);
        response.setErrorCode(0);
        response.setTimestamp(System.currentTimeMillis());
        response.setSuccess(true);
        response.setPath(path);
        return response;
    }

    public static <T> ApiResponse<T> error(String message,String error, int errorCode, String path){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setData(null);
        response.setMessage(message);
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setTimestamp(System.currentTimeMillis());
        response.setPath(path);
        return response;
    }
}
