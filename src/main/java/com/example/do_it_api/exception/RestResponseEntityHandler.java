package com.example.do_it_api.exception;

import com.example.do_it_api.exception.custom_exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice

public class RestResponseEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EmailNotVerifiedException.class)
    protected ResponseEntity<Object> handleEmailNotVerified() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .type("EmailNotVerified")
                .timestamp(LocalDateTime.now())
                .message("The email is not verified")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = EmailAlreadyTakenException.class)
    protected ResponseEntity<Object> handleEmailAlreadyTaken() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type("EmailAlreadyTaken")
                .timestamp(LocalDateTime.now())
                .message("The email is already used for different account")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = ConfirmationTokenExpired.class)
    protected ResponseEntity<Object> handleConfirmationTokenExpired() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .type("ConfirmationTokenExpired")
                .timestamp(LocalDateTime.now())
                .message("The confirmation token is expired")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = WrongConfirmationToken.class)
    protected ResponseEntity<Object> handleWrongConfirmationToken() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .type("WrongConfirmationToken")
                .timestamp(LocalDateTime.now())
                .message("The confirmation token is invalid")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = MailException.class)
    protected ResponseEntity<Object> handleMailException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .type("MailException")
                .timestamp(LocalDateTime.now())
                .message("The mail server is not responding")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .type("UserEmailNotFoundException")
                .timestamp(LocalDateTime.now())
                .message("The userEmail is not found")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = InvalidResetPasswordTokenException.class)
    protected ResponseEntity<Object> handleInvalidResetPasswordTokenException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .type("InvalidResetPasswordTokenException")
                .timestamp(LocalDateTime.now())
                .message("The reset password token is invalid")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = ExpiredResetPasswordTokenException.class)
    protected ResponseEntity<Object> handleExpiredResetPasswordTokenException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .type("ExpiredResetPasswordTokenException")
                .timestamp(LocalDateTime.now())
                .message("The reset password token is expired")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = UnknownException.class)
    protected ResponseEntity<Object> handleUnknownException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type("Unknown Exception")
                .timestamp(LocalDateTime.now())
                .message("Please try again later")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .type("AccessDeniedException")
                .timestamp(LocalDateTime.now())
                .message("You are not authorized to access this resource")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .type("NoSuchElementException")
                .timestamp(LocalDateTime.now())
                .message("The resource is not found")
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = InvalidRequestBodyException.class)
    protected ResponseEntity<Object> handleInvalidRequestBodyException() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type("InvalidRequestBodyException")
                .timestamp(LocalDateTime.now())
                .message("The request body is invalid")
                .build();
        return buildResponseEntity(apiError);
    }



    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}