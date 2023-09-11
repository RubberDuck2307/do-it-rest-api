package com.example.ToDoAPI.email;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequestBody {
    @NotNull
    private String email;
}
