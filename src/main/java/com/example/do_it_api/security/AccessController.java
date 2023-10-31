package com.example.do_it_api.security;


import com.example.do_it_api.email.EmailRequest;
import com.example.do_it_api.email.email_confirmation.ResendEmailConfirmationRequestBody;
import com.example.do_it_api.security.oauth.google.GoogleLoginRequest;
import com.example.do_it_api.user.reset_password.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${app.url}", allowCredentials = "true")
@RequestMapping("/api/v1/access")
@RequiredArgsConstructor

public class AccessController {

    private final AccessService accessService;
    @Value("${app.url}")
    private String appURL;
    @Value("${app.domain}")
    private String appDomain;
    @Value("${google.redirect-uri}")
    private String redirectUri;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return accessService.login(request);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return accessService.register(request);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        accessService.logOut(response);
    }

    @GetMapping("/")
    ResponseEntity<?> checkAuth() {
        return accessService.checkAuth();
    }

    @GetMapping("/email/confirmation")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        return accessService.confirmEmail(token);
    }

    @PostMapping("/email/confirmation/resend")
    public ResponseEntity<?> resendEmail(@Valid @RequestBody ResendEmailConfirmationRequestBody body) {
        if (body.getToken() != null) {
            return accessService.resendConfirmEmail(body.getToken());
        } else if (body.getEmail() != null) {
            return accessService.resendConfirmEmailByEmail(body.getEmail());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return accessService.resetPassword(request);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        return accessService.googleLogin(request);
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<?> resetPasswordRequest(@Valid @RequestBody EmailRequest email) {
        return accessService.sendResetPasswordEmail(email.getEmail());
    }

    @GetMapping("/cors")
        public ResponseEntity<String> cors(){
            return new ResponseEntity<>(appURL + " " + appDomain + " " + redirectUri, HttpStatus.OK);
        }

}
