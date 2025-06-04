package com.example.reserva.infra.security;

import com.ea.backend.domain.user.enterprise.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserAuthenticated implements UserDetails {

    private User user;

    public  UserAuthenticated(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (user.getRole()) {
            case ADMIN -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_TEACHER"));
            case TEACHER -> List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
            default -> List.of();
        };
    }


    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getPasswordHash();
    }
}
