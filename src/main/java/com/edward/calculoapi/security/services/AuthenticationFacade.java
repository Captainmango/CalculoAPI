package com.edward.calculoapi.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationFacade {

    Authentication getAuth();
    UserDetails getCurrentUser();
}
