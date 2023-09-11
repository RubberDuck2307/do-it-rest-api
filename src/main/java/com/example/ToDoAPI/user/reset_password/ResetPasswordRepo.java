package com.example.ToDoAPI.user.reset_password;

import com.example.ToDoAPI.user.DefaultUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordRepo extends JpaRepository<ResetPasswordToken, Long> {

    void deleteByUser(DefaultUserDetails user);
    boolean existsByUser(DefaultUserDetails user);

    ResetPasswordToken findByUser(DefaultUserDetails user);
    boolean existsByToken(String token);
    Optional<ResetPasswordToken> findByToken(String token);

}
