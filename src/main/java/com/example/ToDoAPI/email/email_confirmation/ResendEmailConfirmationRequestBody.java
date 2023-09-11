package com.example.ToDoAPI.email.email_confirmation;

import lombok.Data;

@Data
public class ResendEmailConfirmationRequestBody {


    private String email;
    private String token;
}
