package com.example.ToDoAPI.email.email_confirmation.response;

import com.example.ToDoAPI.user.DefaultUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailConfirmationReturn {

    private DefaultUserDetails user;

    private EmailConfirmationReturnOptions state;
}
