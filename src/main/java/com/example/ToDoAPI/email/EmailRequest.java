package com.example.ToDoAPI.email;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {
    @NotNull
    private String email;
}
