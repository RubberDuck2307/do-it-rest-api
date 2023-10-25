package com.example.do_it_api.security.oauth.google;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String picture;
    private String email;
    private String verified_email;
    private String id;

}
