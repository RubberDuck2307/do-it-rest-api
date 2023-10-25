package com.example.do_it_api.email.email_confirmation.response;

import com.example.do_it_api.user.DefaultUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailConfirmationReturn {

    private DefaultUserDetails user;

    private EmailConfirmationReturnOptions state;
}
