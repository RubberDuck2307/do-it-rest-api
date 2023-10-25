package com.example.do_it_api.security;

import com.example.do_it_api.email.email_confirmation.EmailToken;
import com.example.do_it_api.email.email_confirmation.EmailTokenService;
import com.example.do_it_api.exception.custom_exception.EmailAlreadyTakenException;
import com.example.do_it_api.exception.custom_exception.EmailNotVerifiedException;
import com.example.do_it_api.jwt.JwtService;
import com.example.do_it_api.security.oauth.google.GoogleLoginRequest;
import com.example.do_it_api.security.oauth.google.GoogleOAuthService;
import com.example.do_it_api.user.DefaultUserDetails;
import com.example.do_it_api.user.UserDetailsRepo;
import com.example.do_it_api.user.reset_password.ResetPasswordRequest;
import com.example.do_it_api.user.reset_password.ResetPasswordService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EntityAccessHelper<?> entityAccessHelper;
    private final EmailTokenService emailTokenService;
    private final ResetPasswordService resetPasswordService;
    private final GoogleOAuthService googleOAuthService;
    @Value("${app.domain}")
    private String domain;
    public ResponseEntity<AuthenticationResponse> login(LoginRequest request) {
        Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (a.getPrincipal() instanceof DefaultUserDetails userDetails) {
            if (!userDetails.isEmailConfirmed()) {
                throw new EmailNotVerifiedException();
            }
        }
        return setAccessCookieResponse(request.getEmail());
    }

    public ResponseEntity<String> register(RegisterRequest request) {
        DefaultUserDetails user = new DefaultUserDetails(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        if (userRepo.existsByUserEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        userRepo.save(user);
        EmailToken token = emailTokenService.createToken(user);
        emailTokenService.sendConfirmationEmail(token);
        return new ResponseEntity<>("User created, please confirm your email", HttpStatus.CREATED);
    }

    private ResponseEntity<AuthenticationResponse> setAccessCookieResponse(String email) {
        final String jwt = jwtService.generateToken(email);
        ResponseCookie springCookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .path("/")
                .domain(domain)
                .maxAge(60 * 60 * 24 - 60)
                .sameSite("None")
                .secure(true)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();
    }

    public void logOut(HttpServletResponse response) {
        ResponseCookie springCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .domain(domain)
                .sameSite("None")
                .secure(true)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, springCookie.toString());
    }

    public ResponseEntity<?> checkAuth() {
        DefaultUserDetails defaultUserDetails = entityAccessHelper.getLoggedUser();
        if (Objects.isNull(defaultUserDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<AuthenticationResponse> confirmEmail(String token) {
        DefaultUserDetails userDetails = emailTokenService.confirmEmail(token);
        return setAccessCookieResponse(userDetails.getUsername());
    }

    public ResponseEntity<String> resendConfirmEmail(String token) {
        emailTokenService.resendActivationToken(token);
        return ResponseEntity.ok("Email sent");
    }

    public ResponseEntity<String> resendConfirmEmailByEmail(String email) {
        emailTokenService.resendActivationTokenByEmail(email);
        return ResponseEntity.ok("Email sent");
    }

    public ResponseEntity<String> sendResetPasswordEmail(String email) {
        DefaultUserDetails userDetails = userRepo.findByUserEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        if (userDetails.isOAuth())
            resetPasswordService.sendSetPasswordEmail(userDetails);
        else
            resetPasswordService.sendResetPasswordEmail(resetPasswordService.createToken(userDetails));
        return ResponseEntity.ok("Email sent");
    }

    public ResponseEntity<String> resetPassword(ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request);
        return ResponseEntity.ok("Password changed");
    }

    public ResponseEntity<AuthenticationResponse> googleLogin(GoogleLoginRequest request) {
        String token = googleOAuthService.exchangeCodeForAccessToken(request.getCode());
        String email = googleOAuthService.getGoogleUserInfo(token);
        if (userRepo.existsByUserEmail(email)) {
            return setAccessCookieResponse(email);
        }
        DefaultUserDetails user = new DefaultUserDetails(email, true);
        userRepo.save(user);
        return setAccessCookieResponse(email);
    }


}
