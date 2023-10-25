package com.example.do_it_api.security;

import com.example.do_it_api.exception.custom_exception.UserNotLoggedInException;
import com.example.do_it_api.user.DefaultUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class EntityAccessHelper<T extends PrivateEntity> {
     public DefaultUserDetails getLoggedUser() {
         if (SecurityContextHolder.getContext().getAuthentication() == null){
             throw new UserNotLoggedInException();
         }
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof DefaultUserDetails ?
                (DefaultUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
    }

    public Long getLoggedUserId() {
        return Objects.requireNonNull(getLoggedUser()).getId();
    }

    public boolean hasUserAccessTo(T privateEntity){
        return privateEntity.getUserId().equals(getLoggedUserId());
    }
}
