package com.example.do_it_api.email.email_confirmation;

import com.example.do_it_api.email.EmailDetails;
import com.example.do_it_api.email.EmailService;
import com.example.do_it_api.exception.custom_exception.ConfirmationTokenExpired;
import com.example.do_it_api.exception.custom_exception.WrongConfirmationToken;
import com.example.do_it_api.user.DefaultUserDetails;
import com.example.do_it_api.user.UserDetailsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTokenService {
    @Value("${app.url}")
    private String applicationURL;
    @Value("${application.confirmEmail.url}")
    private String confirmEmailEndpoint;
    private final EmailTokenRepo emailTokenRepo;
    private final EmailService emailService;
    private final UserDetailsRepo userRepo;

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public EmailToken createToken(DefaultUserDetails userDetails) {
        EmailToken emailToken;
        do {
            String token = generateToken();
            emailToken = new EmailToken(token, userDetails);
        }
        while (emailTokenRepo.existsByToken(emailToken.getToken()));
        emailTokenRepo.save(emailToken);
        return emailToken;
    }

    public boolean sendConfirmationEmail(EmailToken token) {
        EmailDetails emailDetails = new EmailDetails(token.getUser().getUsername(),
                applicationURL + confirmEmailEndpoint + token.getToken(), "Confirm your email", null);
        return emailService.sendSimpleMail(emailDetails);
    }

    @Transactional
    public DefaultUserDetails confirmEmail(String token) {
        EmailToken emailToken = emailTokenRepo.findByToken(token).orElseThrow(WrongConfirmationToken::new);
        if (emailToken.getExpirationDate().isBefore(java.time.LocalDateTime.now())) {
            emailTokenRepo.delete(emailToken);
            throw new ConfirmationTokenExpired();
        }
        emailToken.getUser().setEmailConfirmed(true);
        emailTokenRepo.delete(emailToken);
        return  emailToken.getUser();
    }

    public boolean resendActivationToken(String token){
        EmailToken emailToken = emailTokenRepo.findByToken(token).orElseThrow(WrongConfirmationToken::new);
        DefaultUserDetails user = emailToken.getUser();
        emailTokenRepo.delete(emailToken);
        emailToken = createToken(user);

        return sendConfirmationEmail(emailToken);
    }

    public boolean resendActivationTokenByEmail(String email){
        DefaultUserDetails user = userRepo.findByUserEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        EmailToken emailToken = emailTokenRepo.findByUser(user).orElse(null);
        if (!(emailToken == null)) {
            emailTokenRepo.delete(emailToken);
        }
        emailToken = createToken(user);

        return sendConfirmationEmail(emailToken);
    }
}
