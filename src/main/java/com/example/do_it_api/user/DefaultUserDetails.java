package com.example.do_it_api.user;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.event_list.EventList;
import com.example.do_it_api.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "todo", name = "user_credentials")
@Setter
@Getter
public class DefaultUserDetails implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userEmail;
    private String password;
    private boolean oAuth;
    @Column (nullable = false)
    private boolean emailConfirmed = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Event> events;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;

    public DefaultUserDetails(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    public DefaultUserDetails() {

    }

    public DefaultUserDetails(String userEmail, boolean oAuth) {
        emailConfirmed = true;
        this.userEmail = userEmail;
        this.oAuth = oAuth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
