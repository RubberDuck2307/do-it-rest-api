package com.example.ToDoAPI.exception.customException;

public class UnknownException extends RuntimeException{
    public UnknownException(String message) {
        super(message);
    }
}
