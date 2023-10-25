package com.example.do_it_api.user.reset_password;

import com.example.do_it_api.email.EmailDetails;
import com.example.do_it_api.email.EmailService;
import com.example.do_it_api.exception.custom_exception.ExpiredResetPasswordTokenException;
import com.example.do_it_api.exception.custom_exception.InvalidResetPasswordTokenException;
import com.example.do_it_api.user.DefaultUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    @Value("${app.url}")
    private String applicationURL;
    @Value("${application.resetPassword.url}")
    private String resetPasswordPage;
    private final ResetPasswordRepo resetPasswordRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${application.setPassword.url}")
    private String setPasswordPage;

    private String createResetPasswordToken() {
        return UUID.randomUUID().toString();
    }


    public ResetPasswordToken createToken(DefaultUserDetails userDetails) {
            ResetPasswordToken resetPasswordToken;
        do {
            String token = createResetPasswordToken();
            resetPasswordToken = new ResetPasswordToken(userDetails, token);
        }
        while (resetPasswordRepo.existsByToken(resetPasswordToken.getToken()));
        if (resetPasswordRepo.existsByUser(userDetails)) {
            resetPasswordRepo.delete(resetPasswordRepo.findByUser(userDetails));
        }
        resetPasswordRepo.save(resetPasswordToken);
        return resetPasswordToken;
    }

    public boolean sendResetPasswordEmail(ResetPasswordToken token) {
        EmailDetails emailDetails = new EmailDetails(token.getUser().getUsername(), applicationURL + resetPasswordPage + token.getToken(), "Reset your password", null);
        return emailService.sendSimpleMail(emailDetails);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordToken token = resetPasswordRepo.findByToken(resetPasswordRequest.getToken()).orElseThrow(InvalidResetPasswordTokenException::new);
        if (token.getExpirationDate().isBefore(java.time.LocalDateTime.now())) {
            throw new ExpiredResetPasswordTokenException();
        }
        token.getUser().setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        resetPasswordRepo.delete(token);
    }

    public void sendSetPasswordEmail(DefaultUserDetails userDetails) {
        ResetPasswordToken token = createToken(userDetails);
        EmailDetails emailDetails = new EmailDetails(userDetails.getUsername(), "Your account does not have a password, because it has been accessed so far by your google account, you can set your password using the link below \n " + applicationURL + setPasswordPage + token.getToken(), "Set your password", null);
        emailService.sendSimpleMail(emailDetails);
    }
}
