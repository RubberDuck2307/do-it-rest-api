package com.example.do_it_api.email.email_confirmation;

import com.example.do_it_api.exception.custom_exception.EmailNotVerifiedException;
import com.example.do_it_api.exception.custom_exception.UserNotLoggedInException;
import com.example.do_it_api.security.EntityAccessHelper;
import com.example.do_it_api.user.DefaultUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor

public class EmailConfirmedFilter extends OncePerRequestFilter {

    private final EntityAccessHelper<?> entityAccessHelper;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        DefaultUserDetails userDetails;
        try {
            userDetails = entityAccessHelper.getLoggedUser();
        } catch (UserNotLoggedInException e) {
            filterChain.doFilter(request, response);
            return;
        }
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (userDetails.isEmailConfirmed()) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.clearContext();
        throw new EmailNotVerifiedException();
    }
}
