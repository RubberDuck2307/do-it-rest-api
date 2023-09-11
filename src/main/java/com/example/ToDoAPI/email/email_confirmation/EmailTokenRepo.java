package com.example.ToDoAPI.email.email_confirmation;

import com.example.ToDoAPI.email.email_confirmation.EmailToken;
import com.example.ToDoAPI.user.DefaultUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface EmailTokenRepo extends JpaRepository<EmailToken, Long> {
    boolean existsByToken(String token);
    Optional<EmailToken> findByToken(String token);

    Optional<EmailToken> findByUser(DefaultUserDetails user);
}
