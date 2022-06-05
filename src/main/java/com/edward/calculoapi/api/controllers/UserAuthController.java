package com.edward.calculoapi.api.controllers;

import com.edward.calculoapi.api.dto.requests.CreateAccountRequest;
import com.edward.calculoapi.api.dto.requests.LogInRequest;
import com.edward.calculoapi.api.dto.requests.TokenRefreshRequest;
import com.edward.calculoapi.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LogInRequest logInRequest) {
        return userAuthService.loginUser(logInRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        LogInRequest request = userAuthService.createUserAccount(createAccountRequest);
        return userAuthService.loginUser(request);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> loginWithRefresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return userAuthService.loginWithRefresh(tokenRefreshRequest);
    }

}
