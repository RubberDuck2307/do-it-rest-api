package com.example.do_it_api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DatabaseUserDetailService  implements UserDetailsService {

    private final UserDetailsRepo userDetailsRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDetailsRepo.findByUserEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
