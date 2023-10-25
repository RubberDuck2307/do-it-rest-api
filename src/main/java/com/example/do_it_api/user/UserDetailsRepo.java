package com.example.do_it_api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<DefaultUserDetails, Long>
{
    Optional<DefaultUserDetails> findByUserEmail(String username);
    boolean existsByUserEmail(String username);
}
