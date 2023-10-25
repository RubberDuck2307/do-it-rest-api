package com.example.do_it_api.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwt;
    private String error;
    public AuthenticationResponse(String error) {
        this.error = error;
    }
}
