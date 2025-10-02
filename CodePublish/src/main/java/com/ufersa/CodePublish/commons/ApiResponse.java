package com.ufersa.CodePublish.commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    private int errorCode;
    private long timestamp;
    private String path;
}
