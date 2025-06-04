package com.example.reserva.infra.security;

import com.ea.backend.domain.user.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var user = this.userRepository.findUserByEmail(email);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserAuthenticated(user.get());
    }
}
