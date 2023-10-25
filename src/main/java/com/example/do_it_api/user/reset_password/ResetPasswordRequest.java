package com.example.do_it_api.user.reset_password;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Size(max = 255, message = "{validation.name.size.too_long}")
    @NotNull
    private String token;
    @Size(max = 255, message = "{validation.name.size.too_long}")
    @NotNull
    private String password;

}
