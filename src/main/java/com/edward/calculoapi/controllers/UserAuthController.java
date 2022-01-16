package com.edward.calculoapi.controllers;

import com.edward.calculoapi.database.dto.requests.CreateAccountRequest;
import com.edward.calculoapi.database.dto.requests.LogInRequest;
import com.edward.calculoapi.database.dto.requests.TokenRefreshRequest;
import com.edward.calculoapi.database.dto.responses.LogInResponse;
import com.edward.calculoapi.database.dto.responses.MessageResponse;
import com.edward.calculoapi.database.dto.responses.TokenRefreshResponse;
import com.edward.calculoapi.models.User;
import com.edward.calculoapi.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LogInRequest logInRequest) {
        LogInResponse user = userAuthService.loginUser(logInRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        User user = userAuthService.createUserAccount(createAccountRequest);
        return ResponseEntity.ok(user);

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> loginWithRefresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenRefreshResponse token = userAuthService.loginWithRefresh(tokenRefreshRequest);
        return ResponseEntity.ok(token);
    }

}
