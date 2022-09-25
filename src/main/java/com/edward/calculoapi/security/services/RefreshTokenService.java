package com.edward.calculoapi.security.services;

import com.edward.calculoapi.api.models.User;
import com.edward.calculoapi.database.repositories.RefreshTokenRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.exceptions.TokenRefreshException;
import com.edward.calculoapi.api.models.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    @Value("${edward.app.jwtRefreshExpirationMs}")
    private long refreshTokenDuration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    logger.error("Could not create refresh token as user not found. UserID: {}", userId);
                    return new UsernameNotFoundException("Could not find user");
                }
        );

        RefreshToken refreshToken = new RefreshToken(
                user,
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDuration)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            logger.warn("Token expired {}", token);
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));

        return refreshTokenRepository.save(token);
    }

    public int deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    logger.error("Could not delete refresh token as user not found. UserID: {}", userId);
                    return new UsernameNotFoundException("Could not find user");
                }
        );

        return refreshTokenRepository.deleteByUser(user);
    }
}
