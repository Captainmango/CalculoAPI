package com.edward.calculoapi.utils;

import com.edward.calculoapi.database.repositories.RoleRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.exceptions.RoleNotValidException;
import com.edward.calculoapi.database.models.ERole;
import com.edward.calculoapi.database.models.Role;
import com.edward.calculoapi.database.models.User;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MockUserFactory {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public UserDetailsImpl makeMockUser()
    {
        User user = new User("test", "test", "test2@test2.com", "password");
        if (userRepository.existsByEmail(user.getEmail())) {
            return (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotValidException("This is not a valid role"));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());
    }

    public UserDetailsImpl makeMockAdmin()
    {
        User user = new User("test2", "test2", "test3@test3.com", "password");
        if (userRepository.existsByEmail(user.getEmail())) {
            return (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository
                .findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RoleNotValidException("This is not a valid role"));
        roles.add(adminRole);
        user.setRoles(roles);
        userRepository.save(user);

        return (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());
    }
}
