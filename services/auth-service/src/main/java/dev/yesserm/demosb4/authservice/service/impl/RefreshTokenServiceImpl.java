package dev.yesserm.demosb4.authservice.service.impl;

import dev.yesserm.demosb4.authservice.exception.TokenExpiredException;
import dev.yesserm.demosb4.authservice.exception.UnauthorizedException;
import dev.yesserm.demosb4.authservice.model.RefreshToken;
import dev.yesserm.demosb4.authservice.model.User;
import dev.yesserm.demosb4.authservice.repository.RefreshTokenRepository;
import dev.yesserm.demosb4.authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final long refreshTokenExpiration;

    RefreshTokenServiceImpl(
            RefreshTokenRepository repository,
            @Value("${app.security.jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.repository = repository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    @Transactional
    public RefreshToken create(User user) {
        repository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(generateTokenValue());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setRevoked(false);

        return repository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshToken rotate(String token) {
        RefreshToken current = validate(token);
        current.setRevoked(true);
        return create(current.getUser());
    }

    private RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token is invalid"));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("Refresh token was revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }

        return refreshToken;
    }

    private String generateTokenValue() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
