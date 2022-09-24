package com.edward.calculoapi.services;

import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.api.models.User;
import com.edward.calculoapi.exceptions.ResourceNotSavedException;
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

    public User findUserById(long userId) throws UsernameNotFoundException
    {
        return userRepository.findById(userId)
                .orElseThrow(
                        () ->
                            new UsernameNotFoundException("User not found for this ID.")
                );
    }

    public void saveUser(User user)
    {
        try {
            userRepository.save(user);
        } catch(Exception exception) {
            logger.error("failed to save user. User: {} Error: {}", user, exception.getMessage());
            throw new ResourceNotSavedException("Failed to save entity");
        }
    }
}
