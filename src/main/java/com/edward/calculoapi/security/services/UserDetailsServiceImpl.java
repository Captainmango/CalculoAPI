package com.edward.calculoapi.security.services;

import com.edward.calculoapi.api.models.User;
import com.edward.calculoapi.database.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                () -> {
                    logger.error("[UserDetailsServiceImpl@loadByUsername] failed to load user" +
                            "email: {}", email);
                    return new UsernameNotFoundException("No user found with this email.");
                }
            );
        return UserDetailsImpl.build(user);
    }
}
