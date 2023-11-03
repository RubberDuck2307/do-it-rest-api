package com.example.do_it_api.security.oauth.google;

import com.example.do_it_api.exception.custom_exception.UnknownException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;
    private final String GOOGLE_AUTH_URL = "https://oauth2.googleapis.com/token";

    private final RestTemplate restTemplate;

    public String exchangeCodeForAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        try {
            GoogleTokenResponse token = restTemplate.postForObject(GOOGLE_AUTH_URL, body, GoogleTokenResponse.class);
            return token.getAccess_token();
        } catch (Exception e) {
            throw new UnknownException("Error exchanging code for access token");
        }
    }

    public String getGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserInfoResponse> userInfo = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, entity, UserInfoResponse.class);
            return userInfo.getBody().getEmail();
        } catch (Exception e) {
            throw new UnknownException("Error getting user info");
        }
    }
}
