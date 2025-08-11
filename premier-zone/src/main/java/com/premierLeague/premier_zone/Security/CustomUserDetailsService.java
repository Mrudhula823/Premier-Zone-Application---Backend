package com.premierLeague.premier_zone.Security;

import com.sun.security.auth.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found: " + username));


            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authority)
                    .build();
        } catch (Exception e) {
            e.printStackTrace(); // Print root exception to console
            throw e;
        }
    }

}
