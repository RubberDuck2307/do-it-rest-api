package com.example.do_it_api.security.oauth.google;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class GoogleLoginRequest {

    @NotNull
    private String code;


}
