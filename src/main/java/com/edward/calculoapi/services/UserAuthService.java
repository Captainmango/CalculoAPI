package com.edward.calculoapi.services;

import com.edward.calculoapi.api.dto.requests.CreateAccountRequest;
import com.edward.calculoapi.api.dto.requests.LogInRequest;
import com.edward.calculoapi.api.dto.requests.TokenRefreshRequest;
import com.edward.calculoapi.api.dto.responses.LogInResponse;
import com.edward.calculoapi.exceptions.EmailInUseException;
import com.edward.calculoapi.exceptions.RoleNotValidException;
import com.edward.calculoapi.exceptions.TokenRefreshException;
import com.edward.calculoapi.database.models.ERole;
import com.edward.calculoapi.database.models.RefreshToken;
import com.edward.calculoapi.database.models.Role;
import com.edward.calculoapi.database.models.User;
import com.edward.calculoapi.database.repositories.RoleRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.security.jwt.JwtUtils;
import com.edward.calculoapi.security.services.RefreshTokenService;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<LogInResponse> loginUser(@Valid @RequestBody LogInRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(
                new LogInResponse(
                        userDetails.getId(),
                        userDetails.getFirstName(),
                        userDetails.getEmail(),
                        refreshToken.getToken(),
                        jwtToken
                ));
    }

    public LogInRequest createUserAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        if (userRepository.existsByEmail(createAccountRequest.getEmail())) {
           throw new EmailInUseException("Email already in use");
        }

        User user = new User(
                createAccountRequest.getFirstName(),
                createAccountRequest.getLastName(),
                createAccountRequest.getEmail(),
                encoder.encode(createAccountRequest.getPassword())
                );

        user.setRoles(setRoleForUser(createAccountRequest));
        userRepository.save(user);

        return new LogInRequest(
                createAccountRequest.getEmail(),
                createAccountRequest.getPassword()
        );
    }

    public ResponseEntity<?> loginWithRefresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest){
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    ResponseCookie token = jwtUtils.generateJwtCookieFromEmail(user.getEmail());
                    String jwt = jwtUtils.generateJwtFromEmail(user.getEmail());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, token.toString())
                            .body(new LogInResponse(
                                    user.getId(),
                                    user.getFirstName(),
                                    user.getEmail(),
                                    requestRefreshToken,
                                    jwt
                            ));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    private Set<Role> setRoleForUser(CreateAccountRequest createAccountRequest){
        Set<Role> roles = new HashSet<>();

        if(createAccountRequest.getRoles().contains("user")) {
            Role adminRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotValidException("This is not a valid role"));
            roles.add(adminRole);
        }

        return roles;
    }

    private Set<Role> setAdminRoleForUser(CreateAccountRequest createAccountRequest){
        Set<Role> roles = new HashSet<>();

        if(createAccountRequest.getRoles().contains("admin")) {
            Role adminRole = roleRepository
                    .findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RoleNotValidException("This is not a valid role"));
            roles.add(adminRole);
        }

        return roles;
    }
}
