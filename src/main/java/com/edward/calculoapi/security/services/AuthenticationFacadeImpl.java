package com.edward.calculoapi.security.services;

import com.edward.calculoapi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade{

    @Override
    public Authentication getAuth() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    @Override
    public UserDetails getCurrentUser() {

        return (UserDetailsImpl) this.getAuth()
                .getPrincipal();
    }
}
