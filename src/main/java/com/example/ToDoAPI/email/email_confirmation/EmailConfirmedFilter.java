package com.example.ToDoAPI.email.email_confirmation;

import com.example.ToDoAPI.exception.customException.EmailNotVerifiedException;
import com.example.ToDoAPI.exception.customException.UserNotLoggedInException;
import com.example.ToDoAPI.security.EntityAccessHelper;
import com.example.ToDoAPI.user.DefaultUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor

public class EmailConfirmedFilter extends OncePerRequestFilter {

    private final EntityAccessHelper<?> entityAccessHelper;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        DefaultUserDetails userDetails;
        try {
             userDetails = entityAccessHelper.getLoggedUser();
        }
        catch (UserNotLoggedInException e){
            filterChain.doFilter(request, response);
            return;
        }
        if (userDetails == null){
            filterChain.doFilter(request, response);
            return;
        }
        if (userDetails.isEmailConfirmed()){
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.clearContext();
        throw new EmailNotVerifiedException();
    }
}
