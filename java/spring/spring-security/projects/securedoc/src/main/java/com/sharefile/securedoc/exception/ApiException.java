package com.sharefile.securedoc.exception;

/*
Custom exception
 */
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}