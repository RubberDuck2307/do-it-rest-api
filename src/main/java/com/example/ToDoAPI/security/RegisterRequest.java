package com.example.ToDoAPI.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {

    @Email(message = "{validation.email.invalid}")
    @NotNull
    private String email;
    @NotNull
    private String password;

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
