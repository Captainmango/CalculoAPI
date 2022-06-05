package com.edward.calculoapi.services;

import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.database.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCRUDService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    public UserCRUDService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(long userId) throws UsernameNotFoundException
    {
        return userRepository.findById(userId)
                .orElseThrow(
                        () ->
                            new UsernameNotFoundException("User not found for this ID.")
                );
    }
}
