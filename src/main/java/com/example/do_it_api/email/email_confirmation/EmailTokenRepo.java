package com.example.do_it_api.email.email_confirmation;

import com.example.do_it_api.user.DefaultUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepo extends JpaRepository<EmailToken, Long> {
    boolean existsByToken(String token);
    Optional<EmailToken> findByToken(String token);

    Optional<EmailToken> findByUser(DefaultUserDetails user);
}
