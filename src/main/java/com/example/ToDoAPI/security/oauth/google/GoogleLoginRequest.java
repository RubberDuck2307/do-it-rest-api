package com.example.ToDoAPI.security.oauth.google;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class GoogleLoginRequest {

    @NotNull
    private String code;


}
