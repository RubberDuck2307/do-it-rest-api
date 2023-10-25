package com.example.do_it_api.exception.custom_exception;

public class ExpiredResetPasswordTokenException extends RuntimeException{
    public ExpiredResetPasswordTokenException() {
        super("Reset password token has expired");
    }
}
