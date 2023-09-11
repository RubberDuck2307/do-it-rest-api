package com.example.ToDoAPI.security;

import com.example.ToDoAPI.exception.customException.UserNotLoggedInException;
import com.example.ToDoAPI.user.DefaultUserDetails;
import com.example.ToDoAPI.private_entity.PrivateEntity;
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
