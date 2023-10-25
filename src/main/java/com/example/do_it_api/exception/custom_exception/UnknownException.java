package com.example.do_it_api.exception.custom_exception;

public class UnknownException extends RuntimeException{
    public UnknownException(String message) {
        super(message);
    }
}
