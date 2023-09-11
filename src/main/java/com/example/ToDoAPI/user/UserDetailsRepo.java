package com.example.ToDoAPI.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<DefaultUserDetails, Long>
{
    Optional<DefaultUserDetails> findByUserEmail(String username);
    boolean existsByUserEmail(String username);
}
