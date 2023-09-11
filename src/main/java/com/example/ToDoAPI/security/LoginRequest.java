package com.example.ToDoAPI.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class LoginRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
