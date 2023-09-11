package com.example.ToDoAPI.security.oauth.google;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String picture;
    private String email;
    private String verified_email;
    private String id;

}
