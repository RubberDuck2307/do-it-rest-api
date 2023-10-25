package com.example.do_it_api.email;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {
    @NotNull
    private String email;
}
