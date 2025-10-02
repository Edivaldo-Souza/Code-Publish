package com.ufersa.CodePublish.commons;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResponse<Void>> exceptionHandler(Exception ex, HttpServletRequest request) {

        ApiResponse<Void> response = ResponseUtil
                .error(null,ex.getMessage(),10000,request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
