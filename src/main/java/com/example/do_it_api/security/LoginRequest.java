package com.example.do_it_api.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
