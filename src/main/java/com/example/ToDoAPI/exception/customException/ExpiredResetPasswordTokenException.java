package com.example.ToDoAPI.exception.customException;

public class ExpiredResetPasswordTokenException extends RuntimeException{
    public ExpiredResetPasswordTokenException() {
        super("Reset password token has expired");
    }
}
