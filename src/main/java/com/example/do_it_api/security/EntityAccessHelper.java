package com.example.do_it_api.security;

import com.example.do_it_api.exception.custom_exception.UserNotLoggedInException;
import com.example.do_it_api.user.DefaultUserDetails;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class EntityAccessHelper<T extends PrivateEntity> {
    public DefaultUserDetails getLoggedUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new UserNotLoggedInException();
        }
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return (DefaultUserDetails) userDetails;
        } else {
            throw new TypeMismatchException("Current auth principal is not of type UserDetails");
        }
    }

    public Long getLoggedUserId() {
        return Objects.requireNonNull(getLoggedUser()).getId();
    }

    public boolean hasUserAccessTo(T privateEntity) {
        return privateEntity.getUserId().equals(getLoggedUserId());
    }
}
