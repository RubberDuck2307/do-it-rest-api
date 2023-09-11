package com.example.ToDoAPI.user.reset_password;

import com.example.ToDoAPI.user.DefaultUserDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "todo")
@NoArgsConstructor

public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = DefaultUserDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private DefaultUserDetails user;

    @Column(nullable = false, unique = true)
    private String token;

    private static final int EXPIRATION = 60 * 24;

    private final LocalDateTime expirationDate = setExpirationDate();

    public ResetPasswordToken(DefaultUserDetails user, String token) {
        this.user = user;
        this.token = token;
    }

    private LocalDateTime setExpirationDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }
}
