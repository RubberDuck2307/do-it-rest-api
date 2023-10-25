package com.example.do_it_api.security.oauth.google;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoogleTokenResponse {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;

}
