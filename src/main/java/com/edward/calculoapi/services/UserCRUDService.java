package com.edward.calculoapi.services;

import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCRUDService {

    @Autowired
    private UserRepository userRepository;

    private User findUser(long userId) throws UsernameNotFoundException
    {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found for this ID.")
                );
    }
}
