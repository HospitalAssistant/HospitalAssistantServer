package com.hospital.assistant.security;

import com.hospital.assistant.domain.UserEntity;
import com.hospital.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userService.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("UserEntity not found with email : " + email));

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity user = userService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found with id : " + id));

        return UserPrincipal.create(user);
    }
}
