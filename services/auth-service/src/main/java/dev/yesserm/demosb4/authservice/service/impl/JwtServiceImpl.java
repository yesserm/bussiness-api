package dev.yesserm.demosb4.authservice.service.impl;

import dev.yesserm.demosb4.authservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
class JwtServiceImpl implements JwtService {
    private final SecretKey signingKey;
    private final long accessTokenExpiration;

    JwtServiceImpl(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.access-token-expiration}") long accessTokenExpiration
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public long accessTokenExpiration() {
        return accessTokenExpiration;
    }
}
