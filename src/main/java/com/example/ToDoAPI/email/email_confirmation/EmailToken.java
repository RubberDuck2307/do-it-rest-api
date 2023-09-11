package com.example.ToDoAPI.email.email_confirmation;

import com.example.ToDoAPI.user.DefaultUserDetails;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(schema = "todo", name = "email_token")
@Getter
@Setter
@RequiredArgsConstructor
public class EmailToken {
    private static final int EXPIRATION = 60 * 24;
    private final LocalDateTime expirationDate = setExpirationDate();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = DefaultUserDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private DefaultUserDetails user;

    private LocalDateTime setExpirationDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    public EmailToken(String token, DefaultUserDetails user) {
        this.token = token;
        this.user = user;
    }
}