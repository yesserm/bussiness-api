package dev.yesserm.demosb4.authservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    long accessTokenExpiration();
}
